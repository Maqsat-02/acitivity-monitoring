package kz.iitu.edu.activity.monitoring.dto.project.request;

import kz.iitu.edu.activity.monitoring.entity.ExtraChiefEditor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ProjectUpdateReq {
    private final String name;
    private final String description;
    private final String managerId;
    private final String chiefEditorId;
    private final LocalDate targetDate;
}
