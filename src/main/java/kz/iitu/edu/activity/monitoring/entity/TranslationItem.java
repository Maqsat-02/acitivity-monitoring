package kz.iitu.edu.activity.monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "translation_item")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "text_item_id", referencedColumnName = "id", nullable = false)
    private TextItem textItem;

    @ManyToOne
    @JoinColumn(name = "activity_log_id")
    private ActivityLog activityLog;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(name = "change_ordinal")
    private Integer changeOrdinal;

    @Column(nullable = false)
    private String text;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "translationItem")
    private Remark remark;

    @PrePersist
    private void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }
}
