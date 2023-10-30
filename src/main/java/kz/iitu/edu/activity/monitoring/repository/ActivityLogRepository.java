package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}
