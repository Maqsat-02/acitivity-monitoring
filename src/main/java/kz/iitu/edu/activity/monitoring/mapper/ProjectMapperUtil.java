package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.repository.FirebaseUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectMapperUtil {
    private final FirebaseUserRepository userRepository;

    public ProjectDto entityToDto(Project project) {
        FirebaseUser manager = userRepository.findById(project.getManagerId()).get();
        FirebaseUser chiefEditor = userRepository.findById(project.getChiefEditorId()).get();
        return ProjectMapper.INSTANCE.entitiesToDto(project, manager, chiefEditor);
    }
}
