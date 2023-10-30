package kz.iitu.edu.activity.monitoring.dto.activity.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActivityUpdateByManagerReq {
    private final String title;
    private final String language;
    private final String targetLanguage;
    private final String translatorId;
}
