package com.sth.eventservice.model.dto;

import com.sth.eventservice.model.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String CODENAME;
    private String GUNAME;
    private String TITLE;
    private String STRTDATE;
    private String END_DATE;
    private String PLACE;
    private String USE_FEE;
    private String PLAYER;
    private String PROGRAM;
    private String ORG_LINK;
    private double LOT;
    private double LAT;
    private String AREA_NM;
    private String EVENT_NM;

    public Event toEntity() {
        return Event.builder()
                .CODENAME(CODENAME)
                .GUNAME(GUNAME)
                .TITLE(TITLE)
                .STRTDATE(STRTDATE)
                .END_DATE(END_DATE)
                .PLACE(PLACE)
                .USE_FEE(USE_FEE)
                .PLAYER(PLAYER)
                .PROGRAM(PROGRAM)
                .ORG_LINK(ORG_LINK)
                .LOT(LOT)
                .LAT(LAT)
                .AREA_NM(AREA_NM)
                .EVENT_NM(EVENT_NM)
                .build();
    }
}
