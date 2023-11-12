package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.common.response.UserDto;
import kz.iitu.edu.activity.monitoring.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/projectManagers")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public List<UserDto> getProjectManagers(Pageable pageable) {
        return userService.getProjectManagers(pageable);
    }

    @GetMapping("/translators")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public List<UserDto> getTranslators(Pageable pageable) {
        return userService.getTranslators(pageable);
    }

    @GetMapping("/chiefEditors/free")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public List<UserDto> getFreeChiefEditorsToAssignAsMain(Pageable pageable) {
        return userService.getChiefEditorsNotAssignedAsMainToAnyProject(pageable);
    }

    @GetMapping("/chiefEditors/toAssignAsExtraToProject/{projectId}")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public List<UserDto> getChiefEditorsToAssignAsExtra(@PathVariable("projectId") Long projectId, Pageable pageable) {
        return userService.getChiefEditorsToAssignAsExtraToProject(projectId, pageable);
    }
}
