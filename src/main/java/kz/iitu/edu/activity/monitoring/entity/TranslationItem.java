package kz.iitu.edu.activity.monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "translation_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TranslationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "text_item_id", referencedColumnName = "id")
    private TextItem textItem;

    @ManyToOne
    @JoinColumn(name = "activity_log_id")
    private ActivityLog activityLog;

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
