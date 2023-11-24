package kz.iitu.edu.activity.monitoring.dto.activityLog.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ActivityLogDto {
    private final Long id;

    private final Integer hoursCompleted;

    private final Integer hoursRemaining;

    private final Integer percentageCompleted;
    private final LocalDateTime createdAt;
}
