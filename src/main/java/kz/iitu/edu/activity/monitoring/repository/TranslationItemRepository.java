package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.TranslationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationItemRepository extends JpaRepository<TranslationItem, Long> {
}
