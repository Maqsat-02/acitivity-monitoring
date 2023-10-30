package kz.iitu.edu.activity.monitoring.repository;

import kz.iitu.edu.activity.monitoring.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
