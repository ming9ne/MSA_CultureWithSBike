package com.sth.eventservice.model.dto;

import com.sth.eventservice.controller.CityController;
import com.sth.eventservice.model.entity.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    private String AREA_NM;
    private String EVENT_NM;

    public City toEntity() {
        return City.builder()
                .AREA_NM(AREA_NM)
                .EVENT_NM(EVENT_NM)
                .build();
    }
}
