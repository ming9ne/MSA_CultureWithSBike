package com.sth.congestionservice.repository;

import com.sth.congestionservice.model.entity.PopulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopulationRepositoy extends JpaRepository<PopulationEntity, Integer> {
}
