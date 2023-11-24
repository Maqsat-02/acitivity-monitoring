package kz.iitu.edu.activity.monitoring.dto.activity.response;

import kz.iitu.edu.activity.monitoring.dto.common.response.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ActivityDto {
    private final Long id;
    private final Long projectId;
    private final String projectName;
    private final String title;
    private final String language;
    private final String targetLanguage;
    private final UserDto translator;
    private final String status;
    private final String targetTitle;
    private final Integer totalHoursCompleted;
    private final Integer hoursRemaining;
    private final Integer percentageCompleted;
    private final Boolean docxUploaded;
    private final Boolean isLoggedToday;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
