package com.sth.sbikeservice.repository;


import com.sth.sbikeservice.model.entity.Sbike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SbikeRepository extends JpaRepository<Sbike, String> {

    Optional<Sbike> findByStationId(String stationId);

    Optional<Sbike> findByStationName(String stationName);
}
