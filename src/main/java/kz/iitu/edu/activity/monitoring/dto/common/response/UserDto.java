package kz.iitu.edu.activity.monitoring.dto.common.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private final String id;
    private final String role;
    private final String firstName;
    private final String lastName;
}
