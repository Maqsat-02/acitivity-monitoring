package kz.iitu.edu.activity.monitoring.dto.project.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectCreationReq {
    private final String name;
    private final String description;
    private final String chiefEditorId;
}
