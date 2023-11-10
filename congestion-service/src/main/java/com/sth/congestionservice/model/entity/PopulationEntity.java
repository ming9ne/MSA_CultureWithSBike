package com.sth.congestionservice.model.entity;

import com.sth.congestionservice.model.dto.PopulationDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopulationEntity {
    private int areaNo;
    private int areaPpltnMin;
    private int areaPpltnMax;
    private int fcstPpltnMin;
    private int fcstPpltnMax;

    public PopulationDTO toDto() {
        return PopulationDTO.builder()
                .areaNo(areaNo)
                .areaPpltnMin(areaPpltnMin)
                .areaPpltnMax(areaPpltnMax)
                .fcstPpltnMin(fcstPpltnMin)
                .fcstPpltnMax(fcstPpltnMax)
                .build();
    }
}
