package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.ExtraChiefEditor;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.exception.ChiefEditorAlreadyAssignedAsMainException;
import kz.iitu.edu.activity.monitoring.exception.ChiefEditorBusyInProjectException;
import kz.iitu.edu.activity.monitoring.exception.EntityAlreadyExistsException;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.mapper.ProjectMapper;
import kz.iitu.edu.activity.monitoring.repository.ExtraChiefEditorRepository;
import kz.iitu.edu.activity.monitoring.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ExtraChiefEditorRepository extraChiefEditorRepository;
    private final UserService userService;

    public List<ProjectDto> getAll(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAllByOrderByIdDesc(pageable);
        return projectPage.stream()
                .map(this::entityToDto)
                .toList();
    }

    public ProjectDto getById(Long projectId) {
        return entityToDto(getByIdOrThrow(projectId));
    }

    public ProjectDto create(ProjectCreationReq creationReq, String managerId) {
        if (projectRepository.existsByName(creationReq.getName())) {
            throw new EntityAlreadyExistsException("Project with name " + creationReq.getName() + " already exists");
        }

        Project project = ProjectMapper.INSTANCE.creationReqToEntity(creationReq);
        FirebaseUser manager = userService.getManagerByIdOrThrow(managerId);
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(project.getChiefEditorId());
        throwIfChiefEditorAssignedAsMainToAnyProject(chiefEditor.getId());
        project.setManagerId(managerId);
        Project createdProject = projectRepository.save(project);
        return ProjectMapper.INSTANCE.entitiesToDto(createdProject, manager, chiefEditor, new ArrayList<>());
    }

    public ProjectDto update(Long projectId, ProjectUpdateReq updateReq) {
        Project project = getByIdOrThrow(projectId);

        if (updateReq.getManagerId() != null) {
            userService.getManagerByIdOrThrow(updateReq.getManagerId());
        }

        String oldChiefEditorId = project.getChiefEditorId();
        String newChiefEditorId = updateReq.getChiefEditorId();
        if (newChiefEditorId != null && !Objects.equals(oldChiefEditorId, newChiefEditorId)) {
            throwIfChiefEditorBusyInProject(oldChiefEditorId, projectId);

            userService.getChiefEditorByIdOrThrow(newChiefEditorId);
            throwIfChiefEditorAssignedAsMainToAnyProject(newChiefEditorId);
        }

        ProjectMapper.INSTANCE.updateEntityFromUpdateReq(updateReq, project);
        Project updatedProject = projectRepository.save(project);
        return entityToDto(updatedProject);
    }

    public ProjectDto addExtraChiefEditorToProject(Long projectId, String chiefEditorId) {
        Project project = getByIdOrThrow(projectId);
        if (Objects.equals(chiefEditorId, project.getChiefEditorId())) {
            // TODO: consider adding more specific exception. Or conversely, get rid of too specific exceptions (such as ChiefEditorAlreadyAssignedAsMainException)
            throw new EntityAlreadyExistsException("Chief editor " + chiefEditorId + " is already assigned as main in this project");
        }
        Optional<ExtraChiefEditor> alreadyAssignedXChiefEditor = extraChiefEditorRepository.findByChiefEditorIdAndProject(chiefEditorId, project);
        if (alreadyAssignedXChiefEditor.isPresent()) {
            throw new EntityAlreadyExistsException("Extra chief editor " + chiefEditorId + " already exists in project " + projectId);
        }
        ExtraChiefEditor extraChiefEditor = ExtraChiefEditor.builder()
                .chiefEditorId(chiefEditorId)
                .project(project)
                .build();
        extraChiefEditorRepository.save(extraChiefEditor);
        Project updatedProject = getByIdOrThrow(projectId);
        return entityToDto(updatedProject);
    }

    public ProjectDto removeExtraChiefEditorFromProject(Long projectId, String chiefEditorId) {
        Project project = getByIdOrThrow(projectId);
        userService.getChiefEditorByIdOrThrow(chiefEditorId);
        ExtraChiefEditor extraChiefEditor = extraChiefEditorRepository.findByChiefEditorIdAndProject(chiefEditorId, project)
                .orElseThrow(() -> new EntityNotFoundException("Extra chief editor " + chiefEditorId + " not found in project " + projectId));
        if (userService.isChiefEditorBusyInProject(chiefEditorId, projectId)) {
            throw new ChiefEditorBusyInProjectException(chiefEditorId, projectId);
        }
        extraChiefEditorRepository.delete(extraChiefEditor);
        Project updatedProject = getByIdOrThrow(projectId);
        return entityToDto(updatedProject);
    }

    Project getByIdOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project", projectId));
    }

    private ProjectDto entityToDto(Project project) {
        FirebaseUser manager = userService.getManagerByIdOrThrow(project.getManagerId());
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(project.getChiefEditorId());
        List<FirebaseUser> extraChiefEditors = project.getExtraChiefEditors().stream()
                .map(extraChiefEditor -> userService.getChiefEditorByIdOrThrow(extraChiefEditor.getChiefEditorId()))
                .toList();
        for (FirebaseUser firebaseUser : extraChiefEditors) {
            log.info(firebaseUser.toString());
        }

        return ProjectMapper.INSTANCE.entitiesToDto(project, manager, chiefEditor, extraChiefEditors);
    }

    private void throwIfChiefEditorAssignedAsMainToAnyProject(String chiefEditorId) {
        Optional<Project> projectOptional = projectRepository.findByChiefEditorId(chiefEditorId);
        if (projectOptional.isPresent()) {
            throw new ChiefEditorAlreadyAssignedAsMainException(chiefEditorId, projectOptional.get().getId());
        }
    }

    private void throwIfChiefEditorBusyInProject(String chiefEditorId, Long projectId) {
        if (userService.isChiefEditorBusyInProject(chiefEditorId, projectId)) {
            throw new ChiefEditorBusyInProjectException(chiefEditorId, projectId);
        }
    }
}
