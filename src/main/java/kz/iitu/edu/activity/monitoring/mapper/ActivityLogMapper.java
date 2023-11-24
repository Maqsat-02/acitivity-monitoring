package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogDailyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogWeeklyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.response.ActivityLogDto;
import kz.iitu.edu.activity.monitoring.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActivityLogMapper {
    ActivityLogMapper INSTANCE = Mappers.getMapper(ActivityLogMapper.class);

    @Mapping(source = "activityLog.id", target = "id")
    ActivityLogDto entitiesToDto(ActivityLog activityLog);

    ActivityLog dailyCreationReqToEntity(ActivityLogDailyCreationReq creationReq);
    ActivityLog weeklyCreationReqToEntity(ActivityLogWeeklyCreationReq creationReq);
}
