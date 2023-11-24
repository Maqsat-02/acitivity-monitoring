package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select count(r) from Review r where r.status in ('TO_DO', 'IN_PROGRESS') " +
            "and r.chiefEditorId = :chiefEditorId and r.activity.project.id = :projectId")
    long countWithStatusTodoOrInProgressByChiefEditorAndProject(String chiefEditorId, Long projectId);

    List<Review> findAllByChiefEditorId(String chiefEditorId);
}
