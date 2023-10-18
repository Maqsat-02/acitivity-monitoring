package kz.iitu.edu.activity.monitoring.controller;

import com.google.firebase.auth.FirebaseAuthException;
import kz.iitu.edu.activity.monitoring.security.Role;
import kz.iitu.edu.activity.monitoring.service.UserManagementService;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static kz.iitu.edu.activity.monitoring.security.Role.PRODUCT_MANAGER;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserManagementService userManagementService;

//    @PreAuthorize(value="hasRole('ROLE_ANONYMOUS')")
    @PostMapping(path = "/user-claims/{uid}/{role}")
    public void setUserClaims(@PathVariable String uid, @PathVariable String role) {
        userManagementService.setUserClaims(uid, Collections.singletonList(role));
    }
}
