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

    public static String ROLE_TRANSLATOR = "TRANSLATOR";
    public static String ROLE_PRODUCT_MANAGER = "PRODUCT_MANAGER";
    public static String ROLE_CHIEF_EDITOR = "CHIEF_EDITOR";
}
