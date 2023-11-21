package com.sth.areaservice.model.dto;

import com.sth.areaservice.model.entity.Area;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaDTO {
    private String areaNm;

    public Area toEntity() {
        return Area.builder()
                .areaNm(areaNm)
                .build();
    }
}
