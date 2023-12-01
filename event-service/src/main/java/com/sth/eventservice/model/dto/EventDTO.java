package com.sth.eventservice.model.dto;

import com.sth.eventservice.model.entity.Event;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EventDTO {
    private String areaNm;
    private String eventNm;
    private String GUNAME;
    private String TITLE;
    private String CODENAME;
    private String STRTDATE;
    private String END_DATE;
    private String PLACE;
    private String PROGRAM;
    private String USE_FEE;
    private String ORG_LINK;
    private Double LOT;
    private Double LAT;
    private String PLAYER;

    public Event toEntity() {
        return Event.builder()
                .areaNm(areaNm)
                .eventNm(eventNm)
                .GUNAME(GUNAME)
                .TITLE(TITLE)
                .CODENAME(CODENAME)
                .STRTDATE(STRTDATE)
                .END_DATE(END_DATE)
                .PLACE(PLACE)
                .PROGRAM(PROGRAM)
                .ORG_LINK(ORG_LINK)
                .LOT(LOT)
                .LAT(LAT)
                .USE_FEE(USE_FEE)
                .PLAYER(PLAYER)
                .build();
    }
}
