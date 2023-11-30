package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByManagerReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByTranslatorReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogDailyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogWeeklyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.response.ActivityLogDto;
import kz.iitu.edu.activity.monitoring.service.ActivityLogService;
import kz.iitu.edu.activity.monitoring.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/activities")
@AllArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
    private final ActivityLogService activityLogService;

    @GetMapping
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public List<ActivityDto> getAll(Pageable page) {
        return activityService.getAll(page);
    }

    @GetMapping("/{activityId}")
    @PreAuthorize(value = "hasAnyRole('PROJECT_MANAGER', 'TRANSLATOR', 'CHIEF_EDITOR')")
    public ActivityDto getById(@PathVariable Long activityId) {
        return activityService.getById(activityId);
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public List<ActivityDto> getActivitiesByProjectId(@PathVariable Long projectId) {
        return activityService.getActivitiesByProjectId(projectId);
    }

    @GetMapping("/translator/{translatorId}")
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'PROJECT_MANAGER')")
    public List<ActivityDto> getActivitiesByTranslatorId(@PathVariable String translatorId) {
        return activityService.getActivitiesByTranslatorId(translatorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ActivityDto create(@RequestBody ActivityCreationReq creationReq) {
        return activityService.create(creationReq);
    }

    @PutMapping("/{activityId}/docx")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public void updateWithDocx(@PathVariable Long activityId, @RequestParam("docx") MultipartFile docxFile) {
        activityService.updateWithDocx(activityId, docxFile);
    }

    @PutMapping("/{activityId}/updateByManager")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ActivityDto updateByManager(@PathVariable Long activityId, @RequestBody ActivityUpdateByManagerReq updateReq) {
        return activityService.updateByManager(activityId, updateReq);
    }

    @PutMapping("/{activityId}/updateByTranslator")
    @PreAuthorize(value = "hasRole('TRANSLATOR')")
    public ActivityDto updateByTranslator(@PathVariable Long activityId, @RequestBody ActivityUpdateByTranslatorReq updateReq) {
        return activityService.updateByTranslator(activityId, updateReq);
    }

    @PutMapping("/{activityId}/updateByManager/status")
    @PreAuthorize(value = "hasRole('PROJECT_MANAGER')")
    public ActivityDto updateStatusByManager(@PathVariable Long activityId, @RequestBody ActivityStatusUpdateReq updateReq) {
        return activityService.updateStatusByManager(activityId, updateReq);
    }

    @PutMapping("/{activityId}/updateByTranslator/status")
    @PreAuthorize(value = "hasRole('TRANSLATOR')")
    public ActivityDto updateStatusByTranslator(@PathVariable Long activityId, @RequestBody ActivityStatusUpdateReq updateReq) {
        return activityService.updateStatusByTranslator(activityId, updateReq);
    }

    @PostMapping("/{activityId}/log/daily")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR')")
    public ActivityLogDto createDailyLog(@PathVariable Long activityId, @RequestBody ActivityLogDailyCreationReq creationReq) {
        return activityLogService.createDailyLog(activityId, creationReq);
    }

    @PostMapping("/{activityId}/log/weekly")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR')")
    public ActivityLogDto createWeeklyLog(@PathVariable Long activityId, @RequestBody ActivityLogWeeklyCreationReq creationReq) {
        return activityLogService.createWeeklyLog(activityId, creationReq);
    }

    @GetMapping("/{activityId}/log")
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR','PROJECT_MANAGER')")
    public List<ActivityLogDto> getActivityLogByActivityId(@PathVariable Long activityId) {
        return activityLogService.getActivityLogsByActivityId(activityId);
    }
}
