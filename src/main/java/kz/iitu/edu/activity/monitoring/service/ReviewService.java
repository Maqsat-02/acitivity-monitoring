package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.remark.request.ReviewStatusUpdateReq;
import kz.iitu.edu.activity.monitoring.dto.review.response.ReviewDto;
import kz.iitu.edu.activity.monitoring.entity.*;
import kz.iitu.edu.activity.monitoring.enums.ActivityStatus;
import kz.iitu.edu.activity.monitoring.enums.ReviewStatus;
import kz.iitu.edu.activity.monitoring.enums.Role;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.exception.InvalidStatusTransitionException;
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

    public ReviewDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review", reviewId));
        return entityToDto(review);
    }

    public ReviewDto createReview(Activity activity) {
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

    public ReviewDto updateReviewStatus(Long reviewId, ReviewStatusUpdateReq updateReq) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review", reviewId));

        ReviewStatus currentStatus = ReviewStatus.valueOf(review.getStatus());
        ReviewStatus newStatus = ReviewStatus.valueOf(updateReq.getStatus());

        if ((isValidNewStatus(currentStatus, newStatus) &&
                (newStatus == ReviewStatus.OK && !activityRepository.hasUntranslatedTextItems(review.getActivity().getId())))) {
            throw new InvalidStatusTransitionException(Role.CHIEF_EDITOR.name(), "Review", currentStatus.name(), newStatus.name());
        }

        review.setStatus(newStatus.name());
        Review updatedReview = reviewRepository.save(review);
        Activity activity = updatedReview.getActivity();
        if (newStatus == ReviewStatus.NEEDS_REVISION) {
            activity.setStatus(ActivityStatus.REVISION.name());
        } else if (newStatus == ReviewStatus.OK) {
            activity.setStatus(ActivityStatus.DONE.name());
        }

        activityRepository.save(activity);
        return entityToDto(updatedReview);
    }

    private boolean isValidNewStatus(ReviewStatus currentStatus, ReviewStatus newStatus) {
        return switch (currentStatus) {
            case TODO -> newStatus == ReviewStatus.IN_PROGRESS;
            case IN_PROGRESS -> newStatus == ReviewStatus.NEEDS_REVISION || newStatus == ReviewStatus.OK;
            default -> false;
        };
    }

    private ReviewDto entityToDto(Review review) {
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(review.getChiefEditorId());
        return ReviewMapper.INSTANCE.entitiesToDto(review, chiefEditor);
    }
}
