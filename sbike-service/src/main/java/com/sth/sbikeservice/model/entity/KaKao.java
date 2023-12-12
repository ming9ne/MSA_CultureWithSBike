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
    private String stationName;

    @Column
    private String origin;

    @Column
    private String destination;

    @Column
    private int distance;

    @Column
    private String stationLatitude;

    @Column
    private String stationLongitude;

    public KakoDTO toDto() {
        return KakoDTO.builder()
                .origin(origin)
                .stationName(stationName)
                .distance(String.valueOf(distance))
                .stationLatitude(stationLatitude)
                .stationLongitude(stationLongitude)
                .build();
    }

}
