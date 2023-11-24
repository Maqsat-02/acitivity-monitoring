package kz.iitu.edu.activity.monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activity")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String html;

    //enum
    @Column(nullable = false)
    private String language;

    //enum
    @Column(name = "target_language", nullable = false)
    private String targetLanguage;

    @Column(name = "translator_id", nullable = false)
    private String translatorId;

    //enum
    @Column(nullable = false)
    private String status;

    @Column(name = "target_title")
    private String targetTitle;

    @Column(name = "target_html", columnDefinition = "TEXT")
    private String targetHtml;

    @Column(name = "hours_completed", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer hoursCompleted;

    @Column(name = "hours_remaining")
    private Integer hoursRemaining;

    @Column(name = "percentage_completed", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer percentageCompleted;

    @Column(name = "total_text_char_count")
    private Integer totalTextCharCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "activity")
    private List<TextItem> textItems;

    @OneToMany(mappedBy = "activity")
    private List<ActivityLog> activityLogs;

    @OneToMany(mappedBy = "activity")
    private List<Review> reviews;

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
