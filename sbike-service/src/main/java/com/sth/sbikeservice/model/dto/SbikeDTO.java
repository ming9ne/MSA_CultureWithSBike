package com.sth.sbikeservice.model.dto;

import com.sth.sbikeservice.model.entity.KaKao;
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
@XmlRootElement(name = "row")
public class SbikeDTO {

    @XmlElement(name = "stationId")
    private String stationId;

    @XmlElement(name = "rackTotCnt")
    private String rackTotCnt;

    @XmlElement(name = "stationName")
    private String stationName;

    @XmlElement(name = "parkingBikeTotCnt")
    private String parkingBikeTotCnt;

    @XmlElement(name = "shared")
    private String shared;

    @XmlElement(name = "stationLatitude")
    private String stationLatitude;

    @XmlElement(name = "stationLongitude")
    private String stationLongitude;

//    private String destination;
//
//    private String origin;



    public Sbike toEntity() {
        return Sbike.builder()
                .stationId(stationId)
                .rackTotCnt(rackTotCnt)
                .stationName(stationName)
                .parkingBikeTotCnt(parkingBikeTotCnt)
                .shared(shared)
                .stationLatitude(Double.parseDouble(stationLatitude))
                .stationLongitude(Double.parseDouble(stationLongitude))
//                .destination(destination)
                .build();
    }

}
