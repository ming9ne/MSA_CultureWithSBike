package com.sth.congestionservice.model.dto;

import com.sth.congestionservice.model.entity.CongestionEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CongestionDTO {
    private int areaNo;
    private int areaCongestLvl;
    private String areaCongestMsg;
    private int FcstCongestLvl;

    public CongestionEntity toEntity() {
        return CongestionEntity.builder()
                .areaNo(areaNo)
                .areaCongestLvl(areaCongestLvl)
                .areaCongestMsg(areaCongestMsg)
                .FcstCongestLvl(FcstCongestLvl)
                .build();
    }
}
