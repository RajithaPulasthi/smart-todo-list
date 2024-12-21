package com.journeygenius.location_microservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// This interface defines the repository for Location entities
@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    @Query("SELECT l FROM Location l WHERE l.name = ?1")
    Location findByName(String name);

}
