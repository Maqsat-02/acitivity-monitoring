package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.Project;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findAllByOrderByIdDesc(@NonNull Pageable pageable);
    List<Activity> findAllByProject(Project project);
    List<Activity> findAllByTranslatorId(String translatorId);
    List<Activity> findActivitiesByIsLoggedTodayTrueOrderByActivityLogsCreatedAt();
    @Query("SELECT CASE WHEN COUNT(ti) > 0 THEN true ELSE false END " +
            "FROM Activity a " +
            "JOIN a.textItems ti " +
            "LEFT JOIN ti.translationItems tr " +
            "WHERE a.id = :activityId AND tr IS NULL")
    boolean hasUntranslatedTextItems(@Param("activityId") Long activityId);
}
