package kz.iitu.edu.activity.monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "review")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "chief_editor_id", nullable = false)
    private String chiefEditorId;

    // enum
    @Column(nullable = false)
    private String status;

    @Column
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "review")
    private List<Remark> remarks;

    @OneToMany(mappedBy = "review")
    private List<TranslationItem> translationItems;

    @PrePersist
    private void setCreatedAtAndUpdatedAt() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void setUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }
}
