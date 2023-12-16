package com.sth.sbikeservice.model.dto;

import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import jakarta.persistence.Column;
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

    private Long eventId;
    private String eventName;
    private String stationName;
    private String distance;


    public KaKao toEntity() {
        return KaKao.builder()
                .distance(Integer.parseInt(distance))
                .eventId(eventId)
                .stationName(stationName)
                .build();
    }

}
