package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.response.ActivityLogDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.ActivityLog;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActivityLogMapper {
    ActivityLogMapper INSTANCE = Mappers.getMapper(ActivityLogMapper.class);

    @Mapping(source = "activityLog.id", target = "id")
    ActivityLogDto entitiesToDto(ActivityLog activityLog);

    ActivityLog creationReqToEntity(ActivityLogCreationReq creationReq);
}
