// Sbike.java

package com.sth.sbikeservice.model.entity;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sbike {

    @Column
    private String stationId;

    @Column
    private String rackTotCnt;

    @Id
    private String stationName;

    @Column
    private String parkingBikeTotCnt;

    @Column
    private String shared;

    @Column
    private String stationLatitude;

    @Column
    private String stationLongitude;

    @Column
    private String destination;

    private String getDestinationString() {
        return stationLongitude+","+stationLatitude;
    }

    public void updateDestination() {
        String newDestination = getDestinationString();
        this.destination = newDestination;
    }

    public SbikeDTO toDto() {
        return SbikeDTO.builder()
                .stationId(stationId)
                .rackTotCnt(rackTotCnt)
                .stationName(stationName)
                .parkingBikeTotCnt(parkingBikeTotCnt)
                .shared(shared)
                .stationLatitude(stationLatitude)
                .stationLongitude(stationLongitude)
                .destination(destination)
                .build();
    }
}
