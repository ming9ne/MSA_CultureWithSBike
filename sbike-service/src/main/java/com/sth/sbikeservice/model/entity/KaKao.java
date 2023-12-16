// KaKao.java
package com.sth.sbikeservice.model.entity;

import com.sth.sbikeservice.model.dto.KakoDTO;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KaKao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long eventId;

    @Column(length = 1000)
    private String eventName;

    @Column(length = 255)
    private String stationName;

    private int distance;


    public KakoDTO toDto() {
        return KakoDTO.builder()
                .distance(String.valueOf(distance))
                .eventId(eventId)
                .eventName(eventName)
                .stationName(stationName)
                .build();
    }

}