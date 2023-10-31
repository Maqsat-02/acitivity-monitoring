package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ProjectDto create(ProjectCreationReq creationReq, Principal managerPrincipal) {
        return projectService.create(creationReq, managerPrincipal.getName());
    }

    @GetMapping("/get")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public List<ProjectDto> getAll(Pageable page) {
        return projectService.getAll(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ProjectDto update(@PathVariable Long id, ProjectUpdateReq updateReq) {
        return projectService.update(id, updateReq);
    }
}
