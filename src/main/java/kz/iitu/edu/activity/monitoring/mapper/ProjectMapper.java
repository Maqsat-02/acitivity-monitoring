package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Project;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(source = "project.id", target = "id")
    @Mapping(source = "manager", target = "manager")
    @Mapping(source = "chiefEditor", target = "chiefEditor")
    @Mapping(source = "extraChiefEditors", target = "extraChiefEditors")
    ProjectDto entitiesToDto(Project project, FirebaseUser manager, FirebaseUser chiefEditor, List<FirebaseUser> extraChiefEditors);

    Project creationReqToEntity(ProjectCreationReq creationReq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdateReq(ProjectUpdateReq updateReq, @MappingTarget Project project);
}
