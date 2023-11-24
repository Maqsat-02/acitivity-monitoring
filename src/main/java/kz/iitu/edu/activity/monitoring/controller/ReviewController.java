package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.review.response.ReviewDto;
import kz.iitu.edu.activity.monitoring.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/chiefEditor/{chiefEditorId}")
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR','PROJECT_MANAGER')")
    public List<ReviewDto> getReviewsByChiefEditorId(@PathVariable String chiefEditorId, Pageable pageable) {
        return reviewService.getReviewsByChiefEditorId(chiefEditorId, pageable);
    }

    /**
     * This endpoint is optional. Implement additional history and traceability by using this endpoint if necessary.
      */
    @GetMapping("/activity/{activityId}")
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR','PROJECT_MANAGER')")
    public List<ReviewDto> getReviewsByActivityId(@PathVariable Long activityId, Pageable pageable) {
        return reviewService.getReviewsByActivityId(activityId, pageable);
    }

    /**
     * This endpoint is optional. Implement additional history and traceability by using this endpoint if necessary.
     * RemarkDto has field reviewId. When a user views remarks of a particular translation item,
     * they can click on a button/link on a remark to view to which review this remark belongs.
     */
    @GetMapping("/{reviewId}")
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR','PROJECT_MANAGER')")
    public ReviewDto getReviewById(@PathVariable Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }
}
