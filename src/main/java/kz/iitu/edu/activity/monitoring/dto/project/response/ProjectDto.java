package kz.iitu.edu.activity.monitoring.dto.project.response;

import kz.iitu.edu.activity.monitoring.dto.common.UserDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectDto {
    private final Long id;
    private final String name;
    private final String description;
    private final UserDto manager;
    private final UserDto chiefEditor;
    private final String createdAt;
    private final String updatedAt;
}