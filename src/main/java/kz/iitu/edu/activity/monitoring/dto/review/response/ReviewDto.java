package kz.iitu.edu.activity.monitoring.dto.review.response;

import kz.iitu.edu.activity.monitoring.dto.common.response.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ReviewDto {
    private final Long id;
    private final Long activityId;
    private final String status;
    private final UserDto chiefEditor;
    private final String comment;
    private final LocalDateTime createdAt;
}