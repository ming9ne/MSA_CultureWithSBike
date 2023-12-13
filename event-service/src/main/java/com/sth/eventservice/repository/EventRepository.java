package com.sth.eventservice.repository;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    public Event findByEventNm(String eventNm);
    public Event findByAreaNm(String areaNm);

    public boolean existsByAreaNm(String areaNm);
    public boolean existsByTitle(String title);

}