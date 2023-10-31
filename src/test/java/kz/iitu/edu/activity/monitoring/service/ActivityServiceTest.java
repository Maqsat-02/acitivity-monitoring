package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByManagerReq;
import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityUpdateByTranslatorReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.entity.*;
import kz.iitu.edu.activity.monitoring.enums.ActivityStatus;
import kz.iitu.edu.activity.monitoring.repository.ActivityRepository;
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

class ActivityServiceTest {
    @Mock
    ActivityRepository activityRepository;
    @Mock
    TextItemRepository textItemRepository;
    @Mock
    ProjectService projectService;
    @Mock
    UserService userService;
    @InjectMocks
    ActivityService activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        // Mock ProjectService
        when(projectService.getByIdOrThrow(anyLong())).thenReturn(new Project());

        // Mock UserService
        when(userService.getTranslatorByIdOrThrow(anyString())).thenReturn(new FirebaseUser("1", "email", "role", "firstName", "lastName"));

        // Create an Activity object to return when save is called
        Activity activity = Activity.builder()
                .id(1L)
                .translatorId("1")
                .status(ActivityStatus.NEW.name())
                .project(Project.builder().build())
                .build();

        // Create a mock for ActivityRepository
        when(activityRepository.save(any())).thenReturn(activity);

        // Create an instance of ActivityService and inject the mock dependencies
        ActivityService activityService = new ActivityService(activityRepository, textItemRepository, projectService, userService);

        // Call the method you want to test
        ActivityCreationReq creationReq = ActivityCreationReq.builder().build();
        ActivityDto result = activityService.create(creationReq);

        // Assert the result
        ActivityDto expected = ActivityDto.builder()
                .id(1L)
                .status(ActivityStatus.NEW.name())
                .build();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUpdateByManager() {
        // Create an Activity object to return when save is called
        Activity activity = Activity.builder()
                .id(1L)
                .status(ActivityStatus.IN_PROGRESS.name())
                .project(Project.builder().build())
                .build();
        when(activityRepository.findById(anyLong())).thenReturn(Optional.ofNullable(activity));
        // Define the behavior for activityRepository mock
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        // Call the method you want to test
        ActivityUpdateByManagerReq updateReq = ActivityUpdateByManagerReq.builder().build();
        ActivityDto result = activityService.updateByManager(1L, updateReq);

        // Assert the result
        ActivityDto expected = ActivityDto
                .builder()
                .id(1L)
                .status(ActivityStatus.IN_PROGRESS.name())
                .build();/* Define the expected ActivityDto based on the update operation */;
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUpdateByTranslator() {
        Activity activity = Activity.builder()
                .id(1L)
                .status(ActivityStatus.IN_PROGRESS.name())
                .project(Project.builder().build())
                .build();
        when(activityRepository.findById(anyLong())).thenReturn(Optional.ofNullable(activity));
        // Define the behavior for activityRepository mock
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        // Call the method you want to test
        ActivityUpdateByTranslatorReq updateReq = new ActivityUpdateByTranslatorReq("aaa");
        ActivityDto result = activityService.updateByTranslator(1L, updateReq);

        // Assert the result
        ActivityDto expected = ActivityDto
                .builder()
                .id(1L)
                .targetTitle("aaa")
                .status(ActivityStatus.IN_PROGRESS.name())
                .build();/* Define the expected ActivityDto based on the update operation */;
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUpdateStatusByManager() {
        Activity activity = Activity.builder()
                .id(1L)
                .status(ActivityStatus.NEW.name())
                .project(Project.builder().build())
                .build();
        when(activityRepository.findById(anyLong())).thenReturn(Optional.ofNullable(activity));
        // Define the behavior for activityRepository mock
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        // Call the method you want to test
        ActivityStatusUpdateReq updateReq = new ActivityStatusUpdateReq(ActivityStatus.TODO.name());
        ActivityDto result = activityService.updateStatusByManager(1L, updateReq);

        // Assert the result
        ActivityDto expected = ActivityDto
                .builder()
                .id(1L)
                .status(ActivityStatus.TODO.name())
                .build();/* Define the expected ActivityDto based on the update operation */;
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUpdateStatusByTranslator() {
        Activity activity = Activity.builder()
                .id(1L)
                .status(ActivityStatus.TODO.name())
                .project(Project.builder().build())
                .build();
        when(activityRepository.findById(anyLong())).thenReturn(Optional.ofNullable(activity));
        // Define the behavior for activityRepository mock
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        // Call the method you want to test
        ActivityStatusUpdateReq updateReq = new ActivityStatusUpdateReq(ActivityStatus.IN_PROGRESS.name());
        ActivityDto result = activityService.updateStatusByTranslator(1L, updateReq);

        // Assert the result
        ActivityDto expected = ActivityDto
                .builder()
                .id(1L)
                .status(ActivityStatus.IN_PROGRESS.name())
                .build();/* Define the expected ActivityDto based on the update operation */;
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetByIdOrThrow() {
        Activity expected = Activity.builder().build();
        when(activityRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        Activity result = activityService.getByIdOrThrow(Long.valueOf(1));
        Assertions.assertEquals(expected, result);
    }
}
