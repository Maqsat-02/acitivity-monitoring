package kz.iitu.edu.activity.monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "project")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Lob
    private String description;

    @Column(name = "manager_id", nullable = false)
    private String managerId;

    @Column(name = "chief_editor_id", nullable = false)
    private String chiefEditorId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="project")
    private Set<Activity> activities;

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