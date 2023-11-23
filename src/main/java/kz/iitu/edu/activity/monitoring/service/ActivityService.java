package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByManagerReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByTranslatorReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import kz.iitu.edu.activity.monitoring.enums.ActivityStatus;
import kz.iitu.edu.activity.monitoring.enums.Role;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.exception.InvalidStatusTransitionException;
import kz.iitu.edu.activity.monitoring.mapper.ActivityMapper;
import kz.iitu.edu.activity.monitoring.repository.ActivityRepository;
import kz.iitu.edu.activity.monitoring.repository.TextItemRepository;
import kz.iitu.edu.activity.monitoring.util.DocxHtmlConverter;
import kz.iitu.edu.activity.monitoring.util.HtmlSplitter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final TextItemRepository textItemRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public List<ActivityDto> getAll(Pageable pageable) {
        Page<Activity> activityPage = activityRepository.findAllByOrderByIdDesc(pageable);
        return activityPage.stream()
                .map(this::entityToDto)
                .toList();
    }

    public ActivityDto getById(Long activityId) {
        return entityToDto(getByIdOrThrow(activityId));
    }

    public List<ActivityDto> getActivitiesByProjectId(Long projectId) {
        Project project = projectService.getByIdOrThrow(projectId);
        return activityRepository.findAllByProject(project).stream().map(this::entityToDto).toList();
    }

    public List<ActivityDto> getActivitiesByTranslatorId(String translatorId) {
        return activityRepository.findAllByTranslatorId(translatorId).stream().map(this::entityToDto).toList();
    }

    public ActivityDto create(ActivityCreationReq creationReq) {
        Project project = projectService.getByIdOrThrow(creationReq.getProjectId());
        Activity activity = ActivityMapper.INSTANCE.creationReqToEntity(creationReq);
        FirebaseUser translator = userService.getTranslatorByIdOrThrow(activity.getTranslatorId());
        activity.setProject(project);
        activity.setStatus(ActivityStatus.TODO.name());
        Activity createdActivity = activityRepository.save(activity);
        return ActivityMapper.INSTANCE.entitiesToDto(createdActivity, translator);
    }

    @Transactional
    public void updateWithDocx(Long activityId, MultipartFile docxFile) {
        Activity activity = getByIdOrThrow(activityId);

        String html = docxFileToHtml(docxFile);
        List<TextItem> textItems = new HtmlSplitter().getTextItems(html);

        int shownOrdinal = 1;
        for (int i = 0; i < textItems.size(); i++) {
            TextItem textItem = textItems.get(i);
            textItem.setActivity(activity);
            textItem.setOrdinal(i + 1);

            if (!StringUtils.isBlank(textItem.getText())) {
                textItem.setShownOrdinal(shownOrdinal);
                shownOrdinal++;
            }
        }

        if (activity.getHtml() != null) { // already updated with DOCX
            textItemRepository.deleteAllByActivity(activity);
        }

        textItemRepository.saveAll(textItems);
        activity.setHtml(html);
        activityRepository.save(activity);
    }

    private String docxFileToHtml(MultipartFile docxFile) {
        try (InputStream docxInputStream = docxFile.getInputStream()) {
            DocxHtmlConverter docxHtmlConverter = new DocxHtmlConverter();
            return docxHtmlConverter.docxToHtml(docxInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ActivityDto updateByManager(Long activityId, ActivityUpdateByManagerReq updateReq) {
        Activity activity = getByIdOrThrow(activityId);
        ActivityMapper.INSTANCE.updateEntityFromManagerUpdateReq(updateReq, activity);
        Activity updatedProject = activityRepository.save(activity);
        return entityToDto(updatedProject);
    }

    public ActivityDto updateByTranslator(Long activityId, ActivityUpdateByTranslatorReq updateReq) {
        Activity activity = getByIdOrThrow(activityId);
        ActivityMapper.INSTANCE.updateEntityFromTranslatorUpdateReq(updateReq, activity);
        Activity updatedActivity = activityRepository.save(activity);
        return entityToDto(updatedActivity);
    }

    public ActivityDto updateStatusByManager(Long activityId, ActivityStatusUpdateReq statusUpdateReq) {
        Activity activity = getByIdOrThrow(activityId);
        // Check if the requested status transition is valid
        ActivityStatus currentStatus = ActivityStatus.valueOf(activity.getStatus());
        ActivityStatus newStatus = ActivityStatus.valueOf(statusUpdateReq.getStatus());
        if (!isValidStatusTransitionByManager(currentStatus, newStatus)) {
            throw new InvalidStatusTransitionException(Role.PROJECT_MANAGER.name(), "Activity",
                    activity.getStatus(), statusUpdateReq.getStatus());
        }

        activity.setStatus(newStatus.name());
        Activity updatedActivity = activityRepository.save(activity);
        return entityToDto(updatedActivity);
    }

    public ActivityDto updateStatusByTranslator(Long activityId, ActivityStatusUpdateReq statusUpdateReq) {
        Activity activity = getByIdOrThrow(activityId);
        ActivityStatus currentStatus = ActivityStatus.valueOf(activity.getStatus());
        ActivityStatus newStatus = ActivityStatus.valueOf(statusUpdateReq.getStatus());
        if (!isValidStatusTransitionByTranslator(currentStatus, newStatus)) {
            throw new InvalidStatusTransitionException(Role.TRANSLATOR.name(), "Activity", currentStatus.name(), newStatus.name());
        }
        activity.setStatus(newStatus.name());
        Activity updatedActivity = activityRepository.save(activity);
        return entityToDto(updatedActivity);
    }

    Activity getByIdOrThrow(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity", activityId));
    }

    private boolean isValidStatusTransitionByManager(ActivityStatus currentStatus, ActivityStatus newStatus) {
        return switch (currentStatus) {
            case TODO, IN_PROGRESS -> newStatus == ActivityStatus.ARCHIVE;
            default -> false;
        };
    }

    private boolean isValidStatusTransitionByTranslator(ActivityStatus currentStatus, ActivityStatus newStatus) {
        return switch (currentStatus) {
            case TODO -> newStatus == ActivityStatus.IN_PROGRESS;
            case IN_PROGRESS, REVISION -> newStatus == ActivityStatus.REVIEW;
            default -> false;
        };
    }

    private ActivityDto entityToDto(Activity activity) {
        FirebaseUser translator = userService.getTranslatorByIdOrThrow(activity.getTranslatorId());
        return ActivityMapper.INSTANCE.entitiesToDto(activity, translator);
    }
}
