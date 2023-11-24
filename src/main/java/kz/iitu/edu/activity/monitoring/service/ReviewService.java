package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.review.response.ReviewDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Review;
import kz.iitu.edu.activity.monitoring.enums.ActivityStatus;
import kz.iitu.edu.activity.monitoring.enums.ReviewStatus;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.mapper.ReviewMapper;
import kz.iitu.edu.activity.monitoring.repository.ActivityRepository;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ActivityRepository activityRepository;

    private final UserService userService;

    public List<ReviewDto> getReviewsByChiefEditorId(String chiefEditorId, Pageable pageable) {
        return reviewRepository.findAllByChiefEditorId(chiefEditorId, pageable).stream()
                .map(this::entityToDto)
                .toList();
    }

    public List<ReviewDto> getReviewsByActivityId(Long activityId, Pageable pageable) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity", activityId));
        return reviewRepository.findAllByActivity(activity, pageable).stream()
                .map(this::entityToDto)
                .toList();
    }

    ReviewDto createReview(Activity activity) {
        if (ActivityStatus.valueOf(activity.getStatus()) != ActivityStatus.REVIEW) {
            throw new IllegalArgumentException("Activity must have " + ActivityStatus.REVIEW.name() + " status");
        }
        Review review = Review.builder()
                .activity(activity)
                .chiefEditorId(activity.getProject().getChiefEditorId())
                .status(ReviewStatus.TODO.name())
                .remarkCount(0)
                .build();
        Review savedReview = reviewRepository.save(review);
        return entityToDto(savedReview);
    }

    private ReviewDto entityToDto(Review review) {
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(review.getChiefEditorId());
        return ReviewMapper.INSTANCE.entitiesToDto(review, chiefEditor);
    }
}
