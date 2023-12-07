// KaKaoRepository.java
package com.sth.sbikeservice.repository;

import com.sth.sbikeservice.model.entity.KaKao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KaKaoRepository extends JpaRepository<KaKao, String> {

    Optional<KaKao> findByStationName(String stationName);
}
