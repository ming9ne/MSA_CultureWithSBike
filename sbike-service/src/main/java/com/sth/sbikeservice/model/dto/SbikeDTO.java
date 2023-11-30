package com.sth.sbikeservice.model.dto;

import com.sth.sbikeservice.model.entity.Sbike;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="row")
public class SbikeDTO {

// rackTotCnt	거치대개수
// stationName	대여소이름
// parkingBikeTotCnt	자전거주차총건수
// shared	거치율
// stationLatitude	위도
// stationLongitude	경도
// stationId	대여소ID


    private String rackTotCnt;

    private String stationName;

    private String parkingBikeTotCnt;

    private String shared;

    private String stationLatitude;

    private String stationLongitude;

    private String stationId;

    public Sbike toEntity() {
        return Sbike.builder()
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
