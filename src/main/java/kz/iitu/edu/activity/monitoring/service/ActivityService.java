package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByManagerReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByTranslatorReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.mapper.ActivityMapper;
import kz.iitu.edu.activity.monitoring.mapper.ProjectMapper;
import kz.iitu.edu.activity.monitoring.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public ActivityDto create(Long projectId, ActivityCreationReq creationReq) {
        Project project = projectService.getByIdOrThrow(projectId);
        Activity activity = ActivityMapper.INSTANCE.creationReqToEntity(creationReq);
        FirebaseUser translator = userService.getTranslatorByIdOrThrow(activity.getTranslatorId());
        activity.setProject(project);
        activity.setStatus("NEW"); // TODO: change to constant
        Activity createdActivity = activityRepository.save(activity);
        return ActivityMapper.INSTANCE.entitiesToDto(createdActivity, translator);
    }

    public ActivityDto updateByManagerReq(Long id, ActivityUpdateByManagerReq updateReq) {
        Activity activity = getByIdOrThrow(id);
        ActivityMapper.INSTANCE.updateEntityFromManagerUpdateReq(updateReq, activity);
        Activity updatedProject = activityRepository.save(activity);
        return entityToDto(updatedProject);
    }

    public ActivityDto updateByTranslatorReq(Long id, ActivityUpdateByTranslatorReq updateReq) {
        Activity activity = getByIdOrThrow(id);
        ActivityMapper.INSTANCE.updateEntityFromTranslatorUpdateReq(updateReq, activity);
        Activity updatedProject = activityRepository.save(activity);
        return entityToDto(updatedProject);
    }

    public ActivityDto updateByStatusReq(Long id, ActivityStatusUpdateReq updateReq) {
        Activity activity = getByIdOrThrow(id);
        ActivityMapper.INSTANCE.updateEntityFromStatusUpdateReq(updateReq, activity);
        Activity updatedProject = activityRepository.save(activity);
        return entityToDto(updatedProject);
    }

    Activity getByIdOrThrow(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity with ID " + id + " does not exist"));
    }

    private ActivityDto entityToDto(Activity activity) {
        FirebaseUser translator = userService.getManagerByIdOrThrow(activity.getTranslatorId());
        return ActivityMapper.INSTANCE.entitiesToDto(activity, translator);
    }
}
