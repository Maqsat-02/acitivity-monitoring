package kz.iitu.edu.activity.monitoring.dto.activityLog.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ActivityLogWeeklyCreationReq {
    private final Integer hoursCompleted;
    private final Integer hoursRemaining;
    private final Long activityId;
}
