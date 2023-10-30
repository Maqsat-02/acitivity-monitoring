package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByManagerReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByTranslatorReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.mapper.ActivityMapper;
import kz.iitu.edu.activity.monitoring.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public ActivityDto create(ActivityCreationReq creationReq) {
        Project project = projectService.getByIdOrThrow(creationReq.getProjectId());
        Activity activity = ActivityMapper.INSTANCE.creationReqToEntity(creationReq);
        FirebaseUser translator = userService.getTranslatorByIdOrThrow(activity.getTranslatorId());
        activity.setProject(project);
        activity.setStatus("NEW"); // TODO: change to constant
        Activity createdActivity = activityRepository.save(activity);
        return ActivityMapper.INSTANCE.entitiesToDto(createdActivity, translator);
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
        // TODO: status validation
        activity.setStatus(statusUpdateReq.getStatus());
        Activity updatedActivity = activityRepository.save(activity);
        return entityToDto(updatedActivity);
    }

    public ActivityDto updateStatusByTranslator(Long id, ActivityStatusUpdateReq statusUpdateReq) {
        Activity activity = getByIdOrThrow(id);
        // TODO: status validation
        activity.setStatus(statusUpdateReq.getStatus());
        Activity updatedActivity = activityRepository.save(activity);
        return entityToDto(updatedActivity);
    }

    Activity getByIdOrThrow(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity with ID " + id + " does not exist"));
    }

    private ActivityDto entityToDto(Activity activity) {
        FirebaseUser translator = userService.getTranslatorByIdOrThrow(activity.getTranslatorId());
        return ActivityMapper.INSTANCE.entitiesToDto(activity, translator);
    }
}
