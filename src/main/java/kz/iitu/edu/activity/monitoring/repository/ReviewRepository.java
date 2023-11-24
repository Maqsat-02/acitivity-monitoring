package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select count(r) from Review r where r.status in ('TO_DO', 'IN_PROGRESS') " +
            "and r.chiefEditorId = :chiefEditorId and r.activity.project.id = :projectId")
    long countWithStatusTodoOrInProgressByChiefEditorAndProject(String chiefEditorId, Long projectId);

    List<Review> findAllByChiefEditorId(String chiefEditorId, Pageable pageable);

    List<Review> findAllByActivity(Activity activity, Pageable pageable);

    Optional<Review> findFirstByActivityOrderByCreatedAtDesc(Activity activity);
}
