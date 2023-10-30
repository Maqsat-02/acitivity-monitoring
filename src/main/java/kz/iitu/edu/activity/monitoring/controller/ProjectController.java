package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize(value="hasRole('PRODUCT_MANAGER')")
    public ProjectDto createProject(ProjectCreationReq creationReq, Principal managerPrincipal) {
        return projectService.createProject(creationReq, managerPrincipal.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize(value="hasRole('PRODUCT_MANAGER')")
    public ProjectDto updateProject(@PathVariable Long id, ProjectUpdateReq updateReq) {
        return projectService.updateProject(id, updateReq);
    }
}
