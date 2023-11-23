package kz.iitu.edu.activity.monitoring.dto.textitem.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TextTranslationItemDto {
    private final Long id;
    private final Integer ordinal;
    private final String text;
    private final String translationText;
}
