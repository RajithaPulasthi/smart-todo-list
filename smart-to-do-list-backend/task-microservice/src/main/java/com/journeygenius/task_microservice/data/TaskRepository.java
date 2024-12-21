package com.journeygenius.task_microservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query(value = "SELECT * FROM tasks WHERE location_id = ?1 LIMIT 1", nativeQuery = true)
    Task findByLocationId(int locationId);
}

