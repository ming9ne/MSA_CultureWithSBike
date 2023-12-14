// KaKao.java
package com.sth.sbikeservice.model.entity;

import com.sth.sbikeservice.model.dto.KakoDTO;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KaKao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255)
    private String stationName;

    @Column(length = 1000)
    private String origin;

    @Column(length = 1000)
    private String destination;

    @Column
    private int distance;

    @Column
    private double stationLatitude;

    @Column
    private double stationLongitude;


    public KakoDTO toDto() {
        return KakoDTO.builder()
                .origin(origin)
                .stationName(stationName)
                .distance(String.valueOf(distance))
                .stationLatitude(String.valueOf(stationLatitude))
                .stationLongitude(String.valueOf(stationLongitude))
                .build();
    }
}
