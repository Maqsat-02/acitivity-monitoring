package kz.iitu.edu.activity.monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "extra_chief_editor")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XChiefEditor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chief_editor_id", nullable = false)
    private String chiefEditorId;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
