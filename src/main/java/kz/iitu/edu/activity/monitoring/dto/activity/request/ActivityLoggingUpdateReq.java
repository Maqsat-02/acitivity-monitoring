package kz.iitu.edu.activity.monitoring.dto.activity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ActivityLoggingUpdateReq {
    private final Integer hoursCompleted;
    private final Integer hoursRemaining;
    private final Integer percentageCompleted;
    private final Boolean isLoggedToday;
}
