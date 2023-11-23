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
    private String areaNm;
    @Column
    private int areaPpltnMin;
    @Column
    private int areaPpltnMax;

    public PopulationDTO toDto() {
        return PopulationDTO.builder()
                .areaNm(areaNm)
                .areaPpltnMin(areaPpltnMin)
                .areaPpltnMax(areaPpltnMax)
                .build();
    }
}
