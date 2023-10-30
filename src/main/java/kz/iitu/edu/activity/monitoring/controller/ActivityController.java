package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/projects/{projectId}/activities")
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityDto createTicket(@PathVariable("projectId") long projectId,
                                    @RequestBody ActivityCreationReq creationReq) {
        return activityService.createActivity(projectId, creationReq);
    }
}
