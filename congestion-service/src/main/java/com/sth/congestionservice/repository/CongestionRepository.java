package com.sth.congestionservice.repository;

import com.sth.congestionservice.model.entity.CongestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CongestionRepository extends JpaRepository<CongestionEntity, Integer> {
}
