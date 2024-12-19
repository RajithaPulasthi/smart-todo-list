package com.journeygenius.location_microservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("SELECT l.longitude, l.latitude FROM Location l WHERE l.id = ?1")
    String[] getCoordinatesById(Integer id);
}

