package com.sth.congestionservice.repository;

import com.sth.congestionservice.model.entity.Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopulationRepositoy extends JpaRepository<Population, Integer> {
}
