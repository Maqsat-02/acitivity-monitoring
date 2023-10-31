package kz.iitu.edu.activity.monitoring.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

class UserManagementServiceTest {
    @Mock
    private FirebaseAuth firebaseAuth;

    @InjectMocks
    private UserManagementService userManagementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSetUserClaims() throws FirebaseAuthException {
        // Arrange
        String uid = "user123";
        List<String> requestedPermissions = Arrays.asList("permission1", "permission2");
        // Act
        userManagementService.setUserClaims(uid, requestedPermissions);

        // Assert
        // Ensure that the setUserClaims method was called with the expected parameters
        Mockito.verify(firebaseAuth).setCustomUserClaims(uid, Map.of("custom_claims", requestedPermissions));
    }
}