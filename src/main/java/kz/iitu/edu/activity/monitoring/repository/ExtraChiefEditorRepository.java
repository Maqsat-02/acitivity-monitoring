package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.ExtraChiefEditor;
import kz.iitu.edu.activity.monitoring.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExtraChiefEditorRepository extends JpaRepository<ExtraChiefEditor, Long> {
    Optional<ExtraChiefEditor> findByChiefEditorIdAndProject(String chiefEditorId, Project project);
}
