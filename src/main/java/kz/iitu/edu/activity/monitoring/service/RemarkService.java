package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.remark.request.RemarkCreationReq;
import kz.iitu.edu.activity.monitoring.dto.remark.response.RemarkDto;
import kz.iitu.edu.activity.monitoring.entity.Remark;
import kz.iitu.edu.activity.monitoring.entity.Review;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import kz.iitu.edu.activity.monitoring.entity.TranslationItem;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.mapper.RemarkMapper;
import kz.iitu.edu.activity.monitoring.repository.RemarkRepository;
import kz.iitu.edu.activity.monitoring.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        TranslationItem latestTranslationItem = textTranslationItemService.getLatestTranslationItem(textItem)
                .orElseThrow(() -> new EntityNotFoundException("Latest TranslationItem of TextItem " + textItemId + " not found"));
        return remarkRepository.findAllByTranslationItemOrderByCreatedAtDesc(latestTranslationItem).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private RemarkDto entityToDto(Remark remark) {
        return RemarkMapper.INSTANCE.entityToDto(remark);
    }
}
