package com.sth.congestionservice.model.entity;

import com.sth.congestionservice.model.dto.PopulationDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Population {
    @Id
    private int areaNo;
    @Column
    private int areaPpltnMin;
    @Column
    private int areaPpltnMax;
    @Column
    private int fcstPpltnMin;
    @Column
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
