package com.sth.congestionservice.model.entity;

import com.sth.congestionservice.model.dto.CongestionDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Congestion {
    @Id
    private int areaNo;
    @Column
    private int areaCongestLvl;
    @Column
    private String areaCongestMsg;
    @Column
    private int fcstCongestLvl;

    public CongestionDTO toDto() {
        return CongestionDTO.builder()
                .areaNo(areaNo)
                .areaCongestLvl(areaCongestLvl)
                .areaCongestMsg(areaCongestMsg)
                .fcstCongestLvl(fcstCongestLvl)
                .build();
    }
}
