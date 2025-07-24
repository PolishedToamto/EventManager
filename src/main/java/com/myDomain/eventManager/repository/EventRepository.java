package com.myDomain.eventManager.repository;

import com.myDomain.eventManager.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value="SELECT * FROM EVENTS WHERE location = :location", nativeQuery = true)
    List<Event> findEventNative(@Param("location") String location);
}
