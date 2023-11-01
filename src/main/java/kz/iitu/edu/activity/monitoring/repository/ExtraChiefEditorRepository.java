package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Project;
import kz.iitu.edu.activity.monitoring.entity.XChiefEditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExtraChiefEditorRepository extends JpaRepository<XChiefEditor, Long> {
    Optional<XChiefEditor> findByChiefEditorIdAndProject(String chiefEditorId, Project project);
}
