package kz.iitu.edu.activity.monitoring.dto.textitem.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TranslationItemDto {
    private final Integer historyOrdinal;
    private final String translationText;
}
