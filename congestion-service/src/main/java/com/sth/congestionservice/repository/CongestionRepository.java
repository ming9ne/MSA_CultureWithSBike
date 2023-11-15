package com.sth.congestionservice.repository;

import com.sth.congestionservice.model.entity.Congestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CongestionRepository extends JpaRepository<Congestion, Integer> {
}
