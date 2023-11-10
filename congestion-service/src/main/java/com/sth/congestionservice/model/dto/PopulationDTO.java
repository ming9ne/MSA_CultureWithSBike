package com.sth.congestionservice.model.dto;

import com.sth.congestionservice.model.entity.PopulationEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopulationDTO {

    private int areaNo;
    private int areaPpltnMin;
    private int areaPpltnMax;
    private int fcstPpltnMin;
    private int fcstPpltnMax;

    public PopulationEntity toEntity() {
        return PopulationEntity.builder()
                .areaNo(areaNo)
                .areaPpltnMin(areaPpltnMin)
                .areaPpltnMax(areaPpltnMax)
                .fcstPpltnMin(fcstPpltnMin)
                .fcstPpltnMax(fcstPpltnMax)
                .build();
    }
}

