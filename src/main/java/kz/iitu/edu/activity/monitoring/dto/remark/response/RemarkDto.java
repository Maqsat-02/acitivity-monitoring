package kz.iitu.edu.activity.monitoring.dto.remark.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class RemarkDto {
    private final Long reviewId;
    private final String remark;
    private final LocalDateTime createdAt;
}
