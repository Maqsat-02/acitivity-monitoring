package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.mapper.ProjectMapper;
import kz.iitu.edu.activity.monitoring.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectDto create(ProjectCreationReq creationReq, String managerId) {
        Project project = ProjectMapper.INSTANCE.creationReqToEntity(creationReq);
        FirebaseUser manager = userService.getManagerByIdOrThrow(managerId);
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(project.getChiefEditorId());
        project.setManagerId(managerId);
        Project createdProject = projectRepository.save(project);
        return ProjectMapper.INSTANCE.entitiesToDto(createdProject, manager, chiefEditor);
    }

    public List<ProjectDto> getAll(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAllByOrderByIdDesc(pageable);

        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projectPage) {
            projectDtoList.add(entityToDto(project));
        }

        return projectDtoList;
    }

    public ProjectDto update(Long id, ProjectUpdateReq updateReq) {
        Project project = getByIdOrThrow(id);
        ProjectMapper.INSTANCE.updateEntityFromUpdateReq(updateReq, project);
        Project updatedProject = projectRepository.save(project);
        return entityToDto(updatedProject);
    }

    Project getByIdOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project with ID " + id + " does not exist"));
    }

    private ProjectDto entityToDto(Project project) {
        FirebaseUser manager = userService.getManagerByIdOrThrow(project.getManagerId());
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(project.getChiefEditorId());
        return ProjectMapper.INSTANCE.entitiesToDto(project, manager, chiefEditor);
    }
}
