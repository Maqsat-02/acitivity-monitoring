package kz.iitu.edu.activity.monitoring.service;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import kz.iitu.edu.activity.monitoring.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final FirebaseAuth firebaseAuth;

    public void setUserClaims(String uid, List<String> requestedPermissions) {
        Map<String, Object> claims = Map.of("custom_claims", requestedPermissions);

        try {
            firebaseAuth.setCustomUserClaims(uid, claims);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }
}
