package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByManagerReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByTranslatorReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.dto.project.request.ProjectUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.project.response.ProjectDto;
import kz.iitu.edu.activity.monitoring.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/projects/{projectId}/activities")
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityDto create(@PathVariable("projectId") long projectId, @RequestBody ActivityCreationReq creationReq) {
        return activityService.create(projectId, creationReq);
    }

    @PutMapping("updateByManager/{id}")
    @PreAuthorize(value = "hasRole('PRODUCT_MANAGER')")
    public ActivityDto update(@PathVariable Long id, ActivityUpdateByManagerReq updateReq) {
        return activityService.updateByManagerReq(id, updateReq);
    }

    @PutMapping("updateByTranslator/{id}")
    @PreAuthorize(value = "hasRole('TRANSLATOR')")
    public ActivityDto update(@PathVariable Long id, ActivityUpdateByTranslatorReq updateReq) {
        return activityService.updateByTranslatorReq(id, updateReq);
    }

    @PutMapping("updateByStatus/{id}")
    @PreAuthorize(value = "hasRole('PRODUCT_MANAGER')")
    public ActivityDto update(@PathVariable Long id, ActivityStatusUpdateReq updateReq) {
        return activityService.updateByStatusReq(id, updateReq);
    }
}
