package kz.iitu.edu.activity.monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "text_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TextItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Column(name = "prefix_tags", nullable = false)
    private String prefixTags;

    @Column(nullable = false)
    private String text;

    @Column(name = "postfix_tags", nullable = false)
    private String postfixTags;

    @Column(name = "is_blank")
    private Boolean isBlank;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "textItem")
    private TranslationItem translationItem;

    @PrePersist
    private void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }
}
