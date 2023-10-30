package kz.iitu.edu.activity.monitoring.dto.project.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectUpdateReq {
    private final String name;
    private final String description;
    private final String managerId;
    private final String chiefEditorId;
}
