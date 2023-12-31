package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.remark.request.RemarkCreationReq;
import kz.iitu.edu.activity.monitoring.dto.remark.response.RemarkDto;
import kz.iitu.edu.activity.monitoring.entity.*;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.mapper.RemarkMapper;
import kz.iitu.edu.activity.monitoring.repository.RemarkRepository;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RemarkService {
    private final RemarkRepository remarkRepository;
    private final ReviewRepository reviewRepository;

    private final TextTranslationItemService textTranslationItemService;

    public RemarkDto createRemark(Long textItemId, RemarkCreationReq creationReq) {
        TextItem textItem = textTranslationItemService.getByIdWithLatestTranslationItemOrThrow(textItemId);
        TranslationItem latestTranslationItem = textTranslationItemService.getLatestTranslationItem(textItem)
                .orElseThrow(() -> new EntityNotFoundException("Latest TranslationItem of TextItem " + textItemId + " not found"));

        Review review = reviewRepository.findFirstByActivityOrderByCreatedAtDesc(textItem.getActivity())
                .orElseThrow(() -> new EntityNotFoundException("Review of the activity not found"));
        review.setRemarkCount(review.getRemarkCount() + 1);
        reviewRepository.save(review);
        Remark remark = Remark.builder()
                .review(review)
                .translationItem(latestTranslationItem)
                .remark(creationReq.getRemark())
                .build();
        Remark savedRemark = remarkRepository.save(remark);
        return entityToDto(savedRemark);
    }

    public List<RemarkDto> getRemarks(Long textItemId) {
        TextItem textItem = textTranslationItemService.getByIdWithLatestTranslationItemOrThrow(textItemId);
        Optional<TranslationItem> latestTranslationItem = textTranslationItemService.getLatestTranslationItem(textItem);
        if (latestTranslationItem.isEmpty()) {
            return new ArrayList<>();
        }
        return remarkRepository.findAllByTranslationItemOrderByCreatedAtDesc(latestTranslationItem.get()).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private RemarkDto entityToDto(Remark remark) {
        return RemarkMapper.INSTANCE.entityToDto(remark);
    }
}
