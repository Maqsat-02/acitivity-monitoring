package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogDailyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.request.ActivityLogWeeklyCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activityLog.response.ActivityLogDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.ActivityLog;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import kz.iitu.edu.activity.monitoring.repository.ActivityLogRepository;
import kz.iitu.edu.activity.monitoring.repository.TextItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ActivityLogServiceTest {
    @Mock
    ActivityLogRepository activityLogRepository;
    @Mock
    TextItemRepository textItemRepository;
    @Mock
    ActivityService activityService;
    @InjectMocks
    ActivityLogService activityLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDailyLog() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testCreateWeeklyLog() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetActivityLogsByActivityId() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testScheduledCheckingActivityLog() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testCalculateHoursRemaining() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testHasDayChanged() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetLastLogCreatedAt() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetByIdOrThrow() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme