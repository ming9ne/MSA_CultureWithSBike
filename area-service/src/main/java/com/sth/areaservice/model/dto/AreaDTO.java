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
    private int areaNo;
    private String areaNm;

    public Area toEntity() {
        return Area.builder()
                .areaNo(areaNo)
                .areaNm(areaNm)
                .build();
    }
}
