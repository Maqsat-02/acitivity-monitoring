package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Project;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByOrderByIdDesc(@NonNull Pageable pageable);

    @Query("select count(p) > 0 from Project p where p.chiefEditorId = :chiefEditorId")
    boolean projectExistsWithChiefEditorId(String chiefEditorId);
    
    boolean existsByName(String name);
}
