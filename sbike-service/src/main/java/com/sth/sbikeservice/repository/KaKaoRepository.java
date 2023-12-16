// KaKaoRepository.java
package com.sth.sbikeservice.repository;

import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KaKaoRepository extends JpaRepository<KaKao, String> {
    int countByEventName(String eventName);

    List<KaKao> findByEventId(Long eventId);


    boolean existsByEventName(String eventName);
}
