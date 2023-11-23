package com.sth.congestionservice.model.dto;

import com.sth.congestionservice.model.entity.Population;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopulationDTO {

    private String areaNm;
    private int areaPpltnMin;
    private int areaPpltnMax;

    public Population toEntity() {
        return Population.builder()
                .areaNm(areaNm)
                .areaPpltnMin(areaPpltnMin)
                .areaPpltnMax(areaPpltnMax)
                .build();
    }
}

