package kz.iitu.edu.activity.monitoring.controller;

import com.google.firebase.auth.FirebaseAuthException;
import kz.iitu.edu.activity.monitoring.security.Role;
import kz.iitu.edu.activity.monitoring.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserManagementService userManagementService;

    @PreAuthorize(value="hasRole('ROLE_ANONYMOUS')")
    @PostMapping(path = "/user-claims/{uid}")
    public void setUserClaims(@PathVariable String uid, @RequestBody List<Role> requestedClaims) {
        userManagementService.setUserClaims(uid, requestedClaims);
    }
}
