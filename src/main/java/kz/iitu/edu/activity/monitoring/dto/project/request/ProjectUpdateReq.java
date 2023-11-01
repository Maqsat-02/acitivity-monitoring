package kz.iitu.edu.activity.monitoring.dto.project.request;

import kz.iitu.edu.activity.monitoring.entity.XChiefEditor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProjectUpdateReq {
    private final String name;
    private final String description;
    private final String managerId;
    private final String chiefEditorId;
    private final List<XChiefEditor> extraChiefEditors;
}
