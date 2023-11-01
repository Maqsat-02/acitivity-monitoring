package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.common.response.ErrorResponseDto;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.exception.ApiException;
import kz.iitu.edu.activity.monitoring.exception.ChiefEditorAlreadyAssignedAsMainException;
import kz.iitu.edu.activity.monitoring.exception.ChiefEditorBusyInProjectException;
import kz.iitu.edu.activity.monitoring.mapper.ProjectMapper;
import kz.iitu.edu.activity.monitoring.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public List<ProjectDto> getAll(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAllByOrderByIdDesc(pageable);
        return projectPage.stream()
                .map(this::entityToDto)
                .toList();
    }

    public ProjectDto getById(Long id) {
        return entityToDto(getByIdOrThrow(id));
    }

    public ProjectDto create(ProjectCreationReq creationReq, String managerId) {
        Project project = ProjectMapper.INSTANCE.creationReqToEntity(creationReq);
        FirebaseUser manager = userService.getManagerByIdOrThrow(managerId);
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(project.getChiefEditorId());
        throwIfChiefEditorAssignedAsMainToAnyProject(chiefEditor.getId());
        project.setManagerId(managerId);
        Project createdProject = projectRepository.save(project);
        return ProjectMapper.INSTANCE.entitiesToDto(createdProject, manager, chiefEditor);
    }

    public ProjectDto update(Long id, ProjectUpdateReq updateReq) {
        Project project = getByIdOrThrow(id);

        if (updateReq.getManagerId() != null) {
            userService.getManagerByIdOrThrow(updateReq.getManagerId());
        }

        String oldChiefEditorId = project.getChiefEditorId();
        String newChiefEditorId = updateReq.getChiefEditorId();
        if (newChiefEditorId != null && !Objects.equals(oldChiefEditorId, newChiefEditorId)) {
            throwIfChiefEditorBusyInProject(oldChiefEditorId, id);

            userService.getChiefEditorByIdOrThrow(newChiefEditorId);
            throwIfChiefEditorAssignedAsMainToAnyProject(newChiefEditorId);
        }

        ProjectMapper.INSTANCE.updateEntityFromUpdateReq(updateReq, project);
        Project updatedProject = projectRepository.save(project);
        return entityToDto(updatedProject);
    }

    Project getByIdOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> {
                    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                            .status(404)
                            .message("Project with ID " + id + " does not exist")
                            .build();
                    throw new ApiException(errorResponseDto);
                });
    }

    private ProjectDto entityToDto(Project project) {
        FirebaseUser manager = userService.getManagerByIdOrThrow(project.getManagerId());
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(project.getChiefEditorId());
        return ProjectMapper.INSTANCE.entitiesToDto(project, manager, chiefEditor);
    }

    private void throwIfChiefEditorAssignedAsMainToAnyProject(String chiefEditorId) {
        if (userService.isChiefEditorAssignedAsMainToAnyProject(chiefEditorId)) {
            throw new ChiefEditorAlreadyAssignedAsMainException(chiefEditorId);
        }
    }

    private void throwIfChiefEditorBusyInProject(String chiefEditorId, Long projectId) {
        if (userService.isChiefEditorBusyInProject(chiefEditorId, projectId)) {
            throw new ChiefEditorBusyInProjectException(chiefEditorId, projectId);
        }
    }
}
