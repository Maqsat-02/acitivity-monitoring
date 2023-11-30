package kz.iitu.edu.activity.monitoring.util;

import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import kz.iitu.edu.activity.monitoring.repository.TextItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityCalculationUtil {
    private final TextItemRepository textItemRepository;

    public int calculatePercentageCompleted(Activity activity) {
        int totalTextCharCount = activity.getTotalTextCharCount();
        int totalTranslationTextCount = getTotalTranslationTextCount(activity.getId());
        int percentageCompleted = totalTextCharCount != 0
                ? (int) Math.round(((double) totalTranslationTextCount / totalTextCharCount) * 100.0)
                : 0;
        // Ensure percentageCompleted is not greater than 100
        return Math.min(percentageCompleted, 100);
    }

    private int getTotalTranslationTextCount(Long activityId) {
        List<TextItem> textItems = textItemRepository
                .findTextItemsByActivityIdAndTranslationItemsCountGreaterThanZero(activityId);
        return textItems.stream()
                .mapToInt(translationItem -> translationItem.getText().length())
                .sum();
    }
}
