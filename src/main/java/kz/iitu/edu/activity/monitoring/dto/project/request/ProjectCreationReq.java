package kz.iitu.edu.activity.monitoring.dto.project.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ProjectCreationReq {
    private final String name;
    private final String description;
    private final String chiefEditorId;
    private final LocalDate targetDate;
}
