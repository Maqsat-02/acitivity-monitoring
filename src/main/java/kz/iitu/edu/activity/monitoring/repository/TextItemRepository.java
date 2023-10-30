package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.TextItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextItemRepository extends JpaRepository<TextItem, Long> {
}
