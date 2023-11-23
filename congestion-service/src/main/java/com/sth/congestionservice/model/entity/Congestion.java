package com.sth.congestionservice.model.entity;

import com.sth.congestionservice.model.dto.CongestionDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Congestion {
    @Id
    private String areaNm;
    @Column
    private String areaCongestLvl;
    @Column
    private String areaCongestMsg;

    public CongestionDTO toDto() {
        return CongestionDTO.builder()
                .areaNm(areaNm)
                .areaCongestLvl(areaCongestLvl)
                .areaCongestMsg(areaCongestMsg)
                .build();
    }
}
