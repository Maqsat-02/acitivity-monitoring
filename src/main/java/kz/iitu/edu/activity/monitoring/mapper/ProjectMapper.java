package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(source = "project.id", target = "id")
    @Mapping(source = "manager", target = "manager")
    @Mapping(source = "chiefEditor", target = "chiefEditor")
    ProjectDto entitiesToDto(Project project, FirebaseUser manager, FirebaseUser chiefEditor);

    Project creationReqToEntity(ProjectCreationReq creationReq);
}
