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
import kz.iitu.edu.activity.monitoring.mapper.ActivityMapper;
import kz.iitu.edu.activity.monitoring.repository.ActivityRepository;
import kz.iitu.edu.activity.monitoring.repository.TextItemRepository;
import kz.iitu.edu.activity.monitoring.util.DocxHtmlConverter;
import kz.iitu.edu.activity.monitoring.util.HtmlSplitter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final TextItemRepository textItemRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public ActivityDto create(ActivityCreationReq creationReq) {
        Project project = projectService.getByIdOrThrow(creationReq.getProjectId());
        Activity activity = ActivityMapper.INSTANCE.creationReqToEntity(creationReq);
        FirebaseUser translator = userService.getTranslatorByIdOrThrow(activity.getTranslatorId());
        activity.setProject(project);
        activity.setStatus(ActivityStatus.NEW.name());
        Activity createdActivity = activityRepository.save(activity);
        return ActivityMapper.INSTANCE.entitiesToDto(createdActivity, translator);
    }

    public void updateWithDocx(Long id, MultipartFile docxFile) {
        Activity activity = getByIdOrThrow(id);

        String html = docxFileToHtml(docxFile);
        List<TextItem> textItems = new HtmlSplitter().getTextItems(html);

        int shownOrdinal = 1;
        for (int ordinal = 1; ordinal <= textItems.size(); ordinal++) {
            TextItem textItem = textItems.get(ordinal);
            textItem.setActivity(activity);
            textItem.setOrdinal(ordinal);

            if (!StringUtils.isBlank(textItem.getText())) {
                textItem.setShownOrdinal(shownOrdinal);
                shownOrdinal++;
            }
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

    public ActivityDto updateByManager(Long id, ActivityUpdateByManagerReq updateReq) {
        Activity activity = getByIdOrThrow(id);
        ActivityMapper.INSTANCE.updateEntityFromManagerUpdateReq(updateReq, activity);
        Activity updatedProject = activityRepository.save(activity);
        return entityToDto(updatedProject);
    }

    public ActivityDto updateByTranslator(Long id, ActivityUpdateByTranslatorReq updateReq) {
        Activity activity = getByIdOrThrow(id);
        ActivityMapper.INSTANCE.updateEntityFromTranslatorUpdateReq(updateReq, activity);
        Activity updatedActivity = activityRepository.save(activity);
        return entityToDto(updatedActivity);
    }

    public ActivityDto updateStatusByManager(Long id, ActivityStatusUpdateReq statusUpdateReq) {
        Activity activity = getByIdOrThrow(id);
        // Check if the requested status transition is valid
        ActivityStatus newStatus = ActivityStatus.valueOf(statusUpdateReq.getStatus());
        if (!(newStatus == ActivityStatus.TODO || newStatus == ActivityStatus.NEW)) {
            throw new RuntimeException("Manager can't update " + activity.getStatus() + " to " + statusUpdateReq.getStatus());
        }

        activity.setStatus(statusUpdateReq.getStatus());
        Activity updatedActivity = activityRepository.save(activity);
        return entityToDto(updatedActivity);
    }

    public ActivityDto updateStatusByTranslator(Long id, ActivityStatusUpdateReq statusUpdateReq) {
        Activity activity = getByIdOrThrow(id);
        ActivityStatus currentStatus = ActivityStatus.valueOf(activity.getStatus());
        ActivityStatus newStatus = ActivityStatus.valueOf(statusUpdateReq.getStatus());
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new RuntimeException("Invalid status transition requested");
        }
        activity.setStatus(newStatus.name());
        Activity updatedActivity = activityRepository.save(activity);
        return entityToDto(updatedActivity);
    }

    Activity getByIdOrThrow(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity with ID " + id + " does not exist"));
    }

    private boolean isValidStatusTransition(ActivityStatus currentStatus, ActivityStatus newStatus) {
        return switch (currentStatus) {
            case TODO -> newStatus == ActivityStatus.IN_PROGRESS;
            case IN_PROGRESS -> newStatus == ActivityStatus.REVIEW || newStatus == ActivityStatus.TODO;
            case IN_PROGRESS_FROM_REVIEW -> newStatus == ActivityStatus.REVIEW;
            default -> false;
        };
    }

    private ActivityDto entityToDto(Activity activity) {
        FirebaseUser translator = userService.getTranslatorByIdOrThrow(activity.getTranslatorId());
        return ActivityMapper.INSTANCE.entitiesToDto(activity, translator);
    }
}
