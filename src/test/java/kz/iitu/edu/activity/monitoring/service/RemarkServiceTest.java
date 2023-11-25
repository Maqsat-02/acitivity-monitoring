package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.remark.request.RemarkCreationReq;
import kz.iitu.edu.activity.monitoring.dto.remark.response.RemarkDto;
import kz.iitu.edu.activity.monitoring.entity.*;
import kz.iitu.edu.activity.monitoring.repository.ActivityLogRepository;
import kz.iitu.edu.activity.monitoring.repository.RemarkRepository;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class RemarkServiceTest {
    @Mock
    ActivityLogRepository activityLogRepository;
    @InjectMocks
    ActivityLogService activityLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRemark() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetRemarks() {
        ActivityLog expected = ActivityLog.builder().build();
        when(activityLogRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ActivityLog result = activityLogService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme