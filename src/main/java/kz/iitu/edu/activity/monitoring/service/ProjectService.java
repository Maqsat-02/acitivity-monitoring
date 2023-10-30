package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.mapper.ProjectMapper;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
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
    private final FirebaseUserRepository userRepository;

    public ProjectDto createProject(ProjectCreationReq creationReq, String managerId) {
        Project project = ProjectMapper.INSTANCE.creationReqToEntity(creationReq);
        FirebaseUser manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager with ID " + managerId + " does not exist"));
        FirebaseUser chiefEditor = userRepository.findById(project.getChiefEditorId())
                .orElseThrow(() -> new RuntimeException("Chief editor with ID " + project.getChiefEditorId() + " does not exist"));
        project.setManagerId(managerId);
        Project createdProject = projectRepository.save(project);
        return ProjectMapper.INSTANCE.entitiesToDto(createdProject, manager, chiefEditor);
    }

    public List<ProjectDto> findAllProjectsOrderedByIdDesc(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAllByOrderByIdDesc(pageable);

        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projectPage) {
            projectDtoList.add(entityToDto(project));
        }

        return projectDtoList;
    }

    public ProjectDto updateProject(Long id, ProjectUpdateReq updateReq) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project with ID " + id + " does not exist"));
        ProjectMapper.INSTANCE.updateEntityFromUpdateReq(updateReq, project);
        Project updatedProject = projectRepository.save(project);
        return entityToDto(updatedProject);
    }

    public ProjectDto entityToDto(Project project) {
        FirebaseUser manager = userRepository.findById(project.getManagerId()).get();
        FirebaseUser chiefEditor = userRepository.findById(project.getChiefEditorId()).get();
        return ProjectMapper.INSTANCE.entitiesToDto(project, manager, chiefEditor);
    }
}
