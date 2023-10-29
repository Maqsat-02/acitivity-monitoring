package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.TranslationItem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TranslationItemRepository extends PagingAndSortingRepository<TranslationItem,Long> {
}
