package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.TextItem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TextItemRepository extends PagingAndSortingRepository<TextItem,Long> {
}
