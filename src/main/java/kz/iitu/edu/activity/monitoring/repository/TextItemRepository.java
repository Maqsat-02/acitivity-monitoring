package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TextItemRepository extends JpaRepository<TextItem, Long> {
    @Query("SELECT t FROM TextItem t " +
            "LEFT JOIN FETCH t.translationItems tr " +
            "WHERE t.activity.id = :activityId AND t.isBlank = false " +
            "AND (SIZE(tr) = 0 OR tr.changeOrdinal = (" +
            "       SELECT MAX(tri.changeOrdinal) FROM TranslationItem tri WHERE tri.textItem = t" +
            ")) " +
            "ORDER BY t.ordinal ASC")
    List<TextItem> findAllWithLatestTranslationItemByActivityId(Long activityId);

    @Query("SELECT t FROM TextItem t " +
            "LEFT JOIN FETCH t.translationItems tr " +
            "WHERE t.id = :id and (SIZE(tr) = 0 OR tr.changeOrdinal = (" +
            "   SELECT MAX(tri.changeOrdinal) FROM TranslationItem tri WHERE tri.textItem = t" +
            "))")
    Optional<TextItem> findByIdWithLatestTranslationItem(Long id);

    @Query("SELECT t FROM TextItem t " +
            "LEFT JOIN FETCH t.translationItems tr " +
            "WHERE t.id = :id " +
            "ORDER BY tr.changeOrdinal DESC")
    Optional<TextItem> findByIdWithTranslationItems(Long id);

    @Query("SELECT DISTINCT ti FROM TextItem ti " +
            "JOIN ti.translationItems tiTranslationItems " +
            "WHERE ti.activity.id = :activityId AND SIZE(tiTranslationItems) > 0")
    List<TextItem> findTextItemsByActivityIdAndTranslationItemsCountGreaterThanZero(@Param("activityId") Long activityId);

    void deleteAllByActivity(Activity activity);
}
