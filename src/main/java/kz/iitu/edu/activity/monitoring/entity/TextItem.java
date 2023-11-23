package kz.iitu.edu.activity.monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "text_item")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Column(name = "shown_ordinal")
    private Integer shownOrdinal;

    @Column(name = "prefix_tags", nullable = false, columnDefinition = "TEXT")
    private String prefixTags;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "postfix_tags", nullable = false, columnDefinition = "TEXT")
    private String postfixTags;

    @Column(name = "is_blank", nullable = false)
    private Boolean isBlank;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "textItem")
    private List<TranslationItem> translationItems;

    @PrePersist
    private void setIsBlankAndCreatedAt() {
        isBlank = StringUtils.isBlank(text);
        createdAt = LocalDateTime.now();
    }
}
