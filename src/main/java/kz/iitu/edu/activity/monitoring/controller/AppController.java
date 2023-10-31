package kz.iitu.edu.activity.monitoring.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping( "/translator")
    @PreAuthorize(value="hasRole('TRANSLATOR')")
    public String testTranslator(Principal principal) {
        return principal.getName();
    }

    @GetMapping( "/project-manager")
    @PreAuthorize(value="hasRole('PROJECT_MANAGER')")
    public String testProductManager(Principal principal) {
        return principal.getName();
    }

    @GetMapping( "/chief-editor")
    @PreAuthorize(value="hasRole('CHIEF_EDITOR')")
    public String testChiefEditor(Principal principal) {
        return principal.getName();
    }
}
