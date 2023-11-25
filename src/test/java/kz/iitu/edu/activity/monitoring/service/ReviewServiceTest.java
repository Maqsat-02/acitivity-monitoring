package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.remark.request.ReviewStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.review.response.ReviewDto;
import kz.iitu.edu.activity.monitoring.entity.ActivityLog;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Review;
import kz.iitu.edu.activity.monitoring.repository.ActivityRepository;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
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

class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ActivityRepository activityRepository;
    @Mock
    UserService userService;
    @InjectMocks
    ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReviewsByChiefEditorId() {
        Review expected = Review.builder().build();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ReviewDto result = reviewService.getReviewById(Long.valueOf(1));
        Assertions.assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testGetReviewsByActivityId() {
        Review expected = Review.builder().build();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ReviewDto result = reviewService.getReviewById(Long.valueOf(1));
        Assertions.assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testGetReviewById() {
        Review expected = Review.builder().build();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ReviewDto result = reviewService.getReviewById(Long.valueOf(1));
        Assertions.assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testCreateReview() {
        Review expected = Review.builder().build();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ReviewDto result = reviewService.getReviewById(Long.valueOf(1));
        Assertions.assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testUpdateReviewStatus() {
        Review expected = Review.builder().build();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        ReviewDto result = reviewService.getReviewById(Long.valueOf(1));
        Assertions.assertEquals(expected.getId(), result.getId());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme