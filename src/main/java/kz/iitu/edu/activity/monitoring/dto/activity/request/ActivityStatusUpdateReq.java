package kz.iitu.edu.activity.monitoring.dto.activity.request;

import kz.iitu.edu.activity.monitoring.constant.ActivityStatus;
import lombok.Data;

@Data
public class ActivityStatusUpdateReq {
    private final ActivityStatus status;
}
