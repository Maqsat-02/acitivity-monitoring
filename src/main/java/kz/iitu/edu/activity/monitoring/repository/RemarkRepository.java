package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Remark;
import kz.iitu.edu.activity.monitoring.entity.TranslationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RemarkRepository extends JpaRepository<Remark, Long> {
    List<Remark> findAllByTranslationItemOrderByCreatedAtDesc(TranslationItem translationItem);
}
