package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.textitem.request.TranslationItemCreationReq;
import kz.iitu.edu.activity.monitoring.dto.textitem.response.TextTranslationItemDto;
import kz.iitu.edu.activity.monitoring.dto.textitem.response.TranslationItemDto;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import kz.iitu.edu.activity.monitoring.entity.TranslationItem;
import kz.iitu.edu.activity.monitoring.exception.EntityNotFoundException;
import kz.iitu.edu.activity.monitoring.mapper.TextTranslationItemMapper;
import kz.iitu.edu.activity.monitoring.repository.TextItemRepository;
import kz.iitu.edu.activity.monitoring.repository.TranslationItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TextTranslationItemService {
    private final TextItemRepository textItemRepository;
    private final TranslationItemRepository translationItemRepository;

    private final ActivityService activityService;

    public List<TextTranslationItemDto> getAll(Long activityId) {
        activityService.getByIdOrThrow(activityId);
        List<TextItem> textItems = textItemRepository.findAllWithLatestTranslationItemByActivityId(activityId);
        return textItems.stream()
                .map(textItem -> {
                    TranslationItem latestTranslationItem =  getLatestTranslationItem(textItem).orElse(null);
                    return TextTranslationItemMapper.INSTANCE.entitiesToDto(textItem, latestTranslationItem);
                })
                .collect(Collectors.toList());
    }

    public TranslationItemDto create(Long textItemId, TranslationItemCreationReq creationReq) {
        TextItem textItem = textItemRepository.findByIdWithLatestTranslationItem(textItemId)
                .orElseThrow(() -> new EntityNotFoundException("TextItem ", textItemId));

        Optional<TranslationItem> latestTranslationItem = getLatestTranslationItem(textItem);
        int latestChangeOrdinal = latestTranslationItem.isPresent() ? latestTranslationItem.get().getChangeOrdinal() : 0;
        TranslationItem translationItem = TranslationItem.builder()
                .textItem(textItem)
                .changeOrdinal(latestChangeOrdinal + 1)
                .text(creationReq.getTranslationText())
                .build();
        TranslationItem savedTranslationItem = translationItemRepository.save(translationItem);
        return TextTranslationItemMapper.INSTANCE.entityToDto(savedTranslationItem);
    }

    public List<TranslationItemDto> getTranslationItemHistory(Long textItemId) {
        TextItem textItem = textItemRepository.findByIdWithTranslationItems(textItemId)
                .orElseThrow(() -> new EntityNotFoundException("TextItem ", textItemId));
        return textItem.getTranslationItems().stream()
                .map(TextTranslationItemMapper.INSTANCE::entityToDto)
                .collect(Collectors.toList());
    }

    private Optional<TranslationItem> getLatestTranslationItem(TextItem textItem) {
        TranslationItem translationItem = textItem.getTranslationItems().isEmpty() ?
                null : textItem.getTranslationItems().get(0);
        return Optional.ofNullable(translationItem);
    }
}
