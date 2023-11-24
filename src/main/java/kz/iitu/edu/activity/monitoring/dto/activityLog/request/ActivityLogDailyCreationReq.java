package kz.iitu.edu.activity.monitoring.dto.activityLog.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ActivityLogDailyCreationReq {
    private final Integer hoursCompleted;
    private final Long activityId;
}
