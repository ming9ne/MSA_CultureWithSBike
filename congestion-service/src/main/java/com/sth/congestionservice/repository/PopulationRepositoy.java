package com.sth.congestionservice.repository;

import com.sth.congestionservice.model.entity.Population;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopulationRepositoy extends JpaRepository<Population, Integer> {
}
