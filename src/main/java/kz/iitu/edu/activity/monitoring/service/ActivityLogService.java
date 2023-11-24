package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityLoggingUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogDailyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogWeeklyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.response.ActivityLogDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.ActivityLog;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.mapper.ActivityLogMapper;
import kz.iitu.edu.activity.monitoring.repository.ActivityLogRepository;
import kz.iitu.edu.activity.monitoring.repository.TextItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final TextItemRepository textItemRepository;
    private final ActivityService activityService;

    public ActivityLogDto createDailyLog(Long activityId, ActivityLogDailyCreationReq activityLogDailyCreationReq) {
        Activity activity = activityService.getByIdOrThrow(activityId);
        ActivityLog activityLog = ActivityLogMapper.INSTANCE.dailyCreationReqToEntity(activityLogDailyCreationReq);
        activityLog.setActivity(activity);
        List<TextItem> textItems = textItemRepository
                .findTextItemsByActivityIdAndTranslationItemsCountGreaterThanZero(activityId);

        int totalTranslationTextCount = textItems.stream()
                .mapToInt(translationItem -> translationItem.getText().length())
                .sum();
        int percentageCompleted = calculatePercentageCompleted(totalTranslationTextCount, activity.getTotalTextCharCount());
        ActivityLoggingUpdateReq loggingUpdateReq = ActivityLoggingUpdateReq.builder()
                .hoursCompleted(calculateHoursCompleted(activityLog))
                .percentageCompleted(percentageCompleted)
                .build();

        activityLog.setPercentageCompleted(percentageCompleted);
        ActivityLog createdActivityLog = activityLogRepository.save(activityLog);

        activityService.updateLogging(activityLog.getActivity().getId(), loggingUpdateReq);

        return entityToDto(createdActivityLog);
    }

    public ActivityLogDto createWeeklyLog(Long activityId, ActivityLogWeeklyCreationReq activityLogWeeklyCreationReq) {
        Activity activity = activityService.getByIdOrThrow(activityId);
        ActivityLog activityLog = ActivityLogMapper.INSTANCE.weeklyCreationReqToEntity(activityLogWeeklyCreationReq);
        activityLog.setActivity(activity);

        List<TextItem> textItems = textItemRepository
                .findTextItemsByActivityIdAndTranslationItemsCountGreaterThanZero(activityId);

        int totalTranslationTextCount = textItems.stream()
                .mapToInt(translationItem -> translationItem.getText().length())
                .sum();
        int percentageCompleted = calculatePercentageCompleted(totalTranslationTextCount, activity.getTotalTextCharCount());
        ActivityLoggingUpdateReq loggingUpdateReq = ActivityLoggingUpdateReq.builder()
                .hoursCompleted(calculateHoursCompleted(activityLog))
                .percentageCompleted(percentageCompleted)
                .hoursRemaining(calculateHoursRemaining(activityLog))
                .build();

        activityLog.setPercentageCompleted(percentageCompleted);
        ActivityLog createdActivityLog = activityLogRepository.save(activityLog);

        activityService.updateLogging(activityLog.getActivity().getId(), loggingUpdateReq);

        return entityToDto(createdActivityLog);
    }

    public List<ActivityLogDto> getActivityLogsByActivityId(Long activityId){
        Activity activity = activityService.getByIdOrThrow(activityId);
        return activity.getActivityLogs().stream().map(this::entityToDto).toList();
    }

    private int calculatePercentageCompleted(int totalTranslationTextCount, int totalTextCharCount) {
        int percentageCompleted = totalTextCharCount != 0
                ? (int) Math.round(((double) totalTranslationTextCount / totalTextCharCount) * 100.0)
                : 0;

        // Ensure percentageCompleted is not greater than 100
        return Math.min(percentageCompleted, 100);
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

    private ActivityLogDto entityToDto(ActivityLog activityLog) {
        return ActivityLogMapper.INSTANCE.entitiesToDto(activityLog);
    }

    ActivityLog getByIdOrThrow(Long activityLogId) {
        return activityLogRepository.findById(activityLogId)
                .orElseThrow(() -> new EntityNotFoundException("ActivityLog", activityLogId));
    }
}
