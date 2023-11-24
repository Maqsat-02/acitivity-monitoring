package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.activity.request.*;
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
    @Mapping(source = "activity.hoursCompleted", target = "totalHoursCompleted")
    @Mapping(expression = "java(activity.getTotalTextCharCount() != null)", target = "docxUploaded")
    ActivityDto entitiesToDto(Activity activity, FirebaseUser translator);

    Activity creationReqToEntity(ActivityCreationReq creationReq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromManagerUpdateReq(ActivityUpdateByManagerReq updateReq, @MappingTarget Activity activity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromTranslatorUpdateReq(ActivityUpdateByTranslatorReq updateReq, @MappingTarget Activity activity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromLoggingUpdateReq(ActivityLoggingUpdateReq updateReq, @MappingTarget Activity activity);
}
