package kz.iitu.edu.activity.monitoring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseUser {
    private String id;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
}
