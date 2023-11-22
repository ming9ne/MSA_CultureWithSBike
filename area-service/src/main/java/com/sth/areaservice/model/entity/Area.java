package com.sth.areaservice.model.entity;

import com.sth.areaservice.model.dto.AreaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Area {
    @Id
    private String areaNm;

    public AreaDTO toDto() {
        return AreaDTO.builder()
                .areaNm(areaNm)
                .build();
    }
}
