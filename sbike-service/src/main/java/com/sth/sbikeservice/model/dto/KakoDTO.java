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
public class KakoDTO {

    private String origin;
    private String stationName;
    private String stationLatitude;
    private String stationLongitude;
    private String distance;




    public KaKao toEntity() {
        return KaKao.builder()
                .stationName(stationName)
                .distance(Integer.parseInt(distance))
                .stationLatitude(Double.parseDouble(stationLatitude))
                .stationLongitude(Double.parseDouble(stationLongitude))
                .build();
    }

}
