package com.sth.eventservice.model.entity;

import com.sth.eventservice.model.dto.CityDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String AREA_NM;
    private String EVENT_NM;

    public CityDTO toDto() {
        return CityDTO.builder()
                .AREA_NM(AREA_NM)
                .EVENT_NM(EVENT_NM)
                .build();
    }
}