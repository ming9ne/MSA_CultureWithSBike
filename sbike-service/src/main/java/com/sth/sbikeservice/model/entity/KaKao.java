// KaKao.java
package com.sth.sbikeservice.model.entity;

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

    public void updateDistance(int distance) {
        this.distance = distance;
    }
}
