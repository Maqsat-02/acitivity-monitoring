package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Remark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemarkRepository extends JpaRepository<Remark, Long> {
}
