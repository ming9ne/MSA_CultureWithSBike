package com.sth.eventservice.repository;

import com.sth.eventservice.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    public Event findByEventNm(String eventNm);

}