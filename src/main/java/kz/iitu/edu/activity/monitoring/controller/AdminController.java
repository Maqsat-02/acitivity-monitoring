package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserManagementService userManagementService;

//    @PreAuthorize(value="hasRole('ANONYMOUS')")
    @PostMapping(path = "/user-claims/{uid}/{role}")
    public void setUserClaims(@PathVariable String uid, @PathVariable String role) {
        userManagementService.setUserClaims(uid, Collections.singletonList(role));
    }
}
