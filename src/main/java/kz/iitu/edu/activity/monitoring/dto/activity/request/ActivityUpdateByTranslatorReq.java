package kz.iitu.edu.activity.monitoring.dto.activity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityUpdateByTranslatorReq {
    private String targetTitle;
}
