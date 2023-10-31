package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByManagerReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByTranslatorReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActivityMapper {
    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    @Mapping(source = "activity.id", target = "id")
    @Mapping(source = "activity.project.id", target = "projectId")
    @Mapping(source = "activity.project.name", target = "projectName")
    @Mapping(source = "translator", target = "translator")
    ActivityDto entitiesToDto(Activity activity, FirebaseUser translator);

    Activity creationReqToEntity(ActivityCreationReq creationReq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromManagerUpdateReq(ActivityUpdateByManagerReq updateReq, @MappingTarget Activity activity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromTranslatorUpdateReq(ActivityUpdateByTranslatorReq updateReq, @MappingTarget Activity activity);
}