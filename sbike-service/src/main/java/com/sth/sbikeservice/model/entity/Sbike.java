package com.sth.sbikeservice.model.entity;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sbike {
    // rackTotCnt	거치대개수
// stationName	대여소이름
// parkingBikeTotCnt	자전거주차총건수
// shared	거치율
// stationLatitude	위도
// stationLongitude	경도
// stationId	대여소ID

    @Id
    private String stationId;
    @Column
    private String rackTotCnt;
    @Column
    private String stationName;
    @Column
    private String parkingBikeTotCnt;
    @Column
    private String shared;
    @Column
    private String stationLatitude;
    @Column
    private String stationLongitude;



    public SbikeDTO toDto() {
        return SbikeDTO.builder()
                .stationId(stationId)
                .rackTotCnt(rackTotCnt)
                .stationName(stationName)
                .parkingBikeTotCnt(parkingBikeTotCnt)
                .shared(shared)
                .stationLatitude(stationLatitude)
                .stationLongitude(stationLongitude)
                .build();
    }
}