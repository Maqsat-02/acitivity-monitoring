package kz.iitu.edu.activity.monitoring.dto.activityLog.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogDailyCreationReq {
    private Integer hoursCompleted;
}
