package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectCreationReq;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public List<ProjectDto> getAll(Pageable page) {
        return projectService.getAll(page);
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('PROJECT_MANAGER', 'TRANSLATOR')")
    public ProjectDto getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ProjectDto create(@RequestBody ProjectCreationReq creationReq, Principal managerPrincipal) {
        return projectService.create(creationReq, managerPrincipal.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ProjectDto update(@PathVariable Long id, @RequestBody ProjectUpdateReq updateReq) {
        return projectService.update(id, updateReq);
    }

    @PostMapping("/{id}/extraChiefEditors/{chiefEditorId}")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ProjectDto addExtraChiefEditorToProject(@PathVariable Long id, @PathVariable String chiefEditorId) {
        return projectService.addExtraChiefEditorToProject(id, chiefEditorId);
    }

    @DeleteMapping("/{id}/extraChiefEditors/{chiefEditorId}")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ProjectDto removeExtraChiefEditorFromProject(@PathVariable("id") Long id, @PathVariable("chiefEditorId") String chiefEditorId) {
        return projectService.removeExtraChiefEditorFromProject(id, chiefEditorId);
    }
}
