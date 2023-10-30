package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByManagerReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByTranslatorReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activities")
@AllArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityDto create(@RequestBody ActivityCreationReq creationReq) {
        return activityService.create(creationReq);
    }

    @PutMapping("/{id}/updateByManager")
    @PreAuthorize(value = "hasRole('PRODUCT_MANAGER')")
    public ActivityDto updateByManager(@PathVariable long id, @RequestBody ActivityUpdateByManagerReq updateReq) {
        return activityService.updateByManager(id, updateReq);
    }

    @PutMapping("/{id}/updateByTranslator")
    @PreAuthorize(value = "hasRole('TRANSLATOR')")
    public ActivityDto updateByTranlator(@PathVariable long id, @RequestBody ActivityUpdateByTranslatorReq updateReq) {
        return activityService.updateByTranslator(id, updateReq);
    }

    @PutMapping("/{id}/updateByManager/status")
    @PreAuthorize(value = "hasRole('PRODUCT_MANAGER')")
    public ActivityDto updateStatusByManager(@PathVariable long id, @RequestBody ActivityStatusUpdateReq updateReq) {
        return activityService.updateStatusByManager(id, updateReq);
    }

    @PutMapping("/{id}/updateByTranslator/status")
    @PreAuthorize(value = "hasRole('TRANSLATOR')")
    public ActivityDto updateStatusByTranslator(@PathVariable long id, @RequestBody ActivityStatusUpdateReq updateReq) {
        return activityService.updateStatusByTranslator(id, updateReq);
    }
}
