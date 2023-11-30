package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityLoggingUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogDailyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogWeeklyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.response.ActivityLogDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.ActivityLog;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.mapper.ActivityLogMapper;
import kz.iitu.edu.activity.monitoring.repository.ActivityLogRepository;
import kz.iitu.edu.activity.monitoring.util.ActivityCalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final ActivityService activityService;
    private final ActivityCalculationUtil calculationUtil;

    public ActivityLogDto createDailyLog(Long activityId, ActivityLogDailyCreationReq activityLogDailyCreationReq) {
        Activity activity = activityService.getByIdOrThrow(activityId);
        activity.setIsLoggedToday(true);
        ActivityLog activityLog = ActivityLogMapper.INSTANCE.dailyCreationReqToEntity(activityLogDailyCreationReq);
        activityLog.setActivity(activity);

        int percentageCompleted = calculationUtil.calculatePercentageCompleted(activity);
        ActivityLoggingUpdateReq loggingUpdateReq = ActivityLoggingUpdateReq.builder()
                .hoursCompleted(calculateHoursCompleted(activityLog))
                .percentageCompleted(percentageCompleted)
                .isLoggedToday(activity.getIsLoggedToday())
                .build();
        activityLog.setPercentageCompleted(percentageCompleted);

        ActivityLog createdActivityLog = activityLogRepository.save(activityLog);
        activityService.updateLogging(activityLog.getActivity().getId(), loggingUpdateReq);
        return entityToDto(createdActivityLog);
    }

    public ActivityLogDto createWeeklyLog(Long activityId, ActivityLogWeeklyCreationReq activityLogWeeklyCreationReq) {
        Activity activity = activityService.getByIdOrThrow(activityId);
        activity.setIsLoggedToday(true);
        ActivityLog activityLog = ActivityLogMapper.INSTANCE.weeklyCreationReqToEntity(activityLogWeeklyCreationReq);
        activityLog.setActivity(activity);

        int percentageCompleted = calculationUtil.calculatePercentageCompleted(activity);
        ActivityLoggingUpdateReq loggingUpdateReq = ActivityLoggingUpdateReq.builder()
                .hoursCompleted(calculateHoursCompleted(activityLog))
                .percentageCompleted(percentageCompleted)
                .hoursRemaining(calculateHoursRemaining(activityLog))
                .isLoggedToday(activity.getIsLoggedToday())
                .build();

        activityLog.setPercentageCompleted(percentageCompleted);
        ActivityLog createdActivityLog = activityLogRepository.save(activityLog);

        activityService.updateLogging(activityLog.getActivity().getId(), loggingUpdateReq);

        return entityToDto(createdActivityLog);
    }

    public List<ActivityLogDto> getActivityLogsByActivityId(Long activityId) {
        Activity activity = activityService.getByIdOrThrow(activityId);
        return activity.getActivityLogs().stream().map(this::entityToDto).toList();
    }

    @Scheduled(fixedDelay = 300000)
    public void scheduledCheckingActivityLog() {
        List<Activity> activities = activityService.getActivitiesIsLoggedTodayTrue();
        activities = activities.stream().
                filter(activity -> hasDayChanged(getLastLogCreatedAt(activity)))
                .toList();

        for (Activity activity : activities) {
            ActivityLoggingUpdateReq loggingUpdateReq = ActivityLoggingUpdateReq.builder()
                    .isLoggedToday(false)
                    .build();
            activityService.updateLogging(activity.getId(), loggingUpdateReq);
        }
    }

    public int calculateHoursRemaining(ActivityLog activityLog) {
        return (activityLog.getHoursRemaining() != null)
                ? activityLog.getHoursRemaining()
                : activityLog.getActivity().getHoursRemaining();
    }

    private int calculateHoursCompleted(ActivityLog activityLog) {
        int hoursCompleted = activityLog.getHoursCompleted();

        if (activityLog.getActivity().getHoursCompleted() != null) {
            hoursCompleted += activityLog.getActivity().getHoursCompleted();
        }
        return hoursCompleted;
    }

    public boolean hasDayChanged(LocalDateTime dateTimeToCheck) {
        if (dateTimeToCheck == null) {
            // Handle the case where the last log creation date is null
            System.out.println("Last log creation date is null.");
            return false;  // Or choose an appropriate behavior
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        if (dateTimeToCheck.getDayOfMonth() == currentDateTime.getDayOfMonth()) {
            System.out.println("The specific date is today.");
            return false;
        } else {
            System.out.println("The specific date is before today's date.");
            return true;
        }
    }

    //    @Transactional
    public LocalDateTime getLastLogCreatedAt(Activity activity) {
        List<ActivityLog> activityLogs = activity.getActivityLogs();
        if (activityLogs != null && !activityLogs.isEmpty()) {
            return activityLogs.get(activityLogs.size() - 1).getCreatedAt();
        }
        return null;
    }

    private ActivityLogDto entityToDto(ActivityLog activityLog) {
        return ActivityLogMapper.INSTANCE.entitiesToDto(activityLog);
    }

    ActivityLog getByIdOrThrow(Long activityLogId) {
        return activityLogRepository.findById(activityLogId)
                .orElseThrow(() -> new EntityNotFoundException("ActivityLog", activityLogId));
    }
}
