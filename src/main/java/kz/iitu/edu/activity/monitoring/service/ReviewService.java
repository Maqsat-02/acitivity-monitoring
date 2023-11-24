package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.review.response.ReviewDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Review;
import kz.iitu.edu.activity.monitoring.mapper.ReviewMapper;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserService userService;

    public List<ReviewDto> getReviewsByTranslatorId(String chiefEditorId) {
        return reviewRepository.findAllByChiefEditorId(chiefEditorId).stream().map(this::entityToDto).toList();
    }

    private ReviewDto entityToDto(Review review) {
        FirebaseUser chiefEditor = userService.getChiefEditorByIdOrThrow(review.getChiefEditorId());
        return ReviewMapper.INSTANCE.entitiesToDto(review, chiefEditor);
    }
}
