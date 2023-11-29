package com.sth.eventservice.model.entity;

import com.sth.eventservice.model.dto.EventDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Event {
    @Id
    private String areaNm;
    @Column
    private String eventNm;
    @Column
    private String GUNAME;
    @Column
    private String TITLE;
    @Column
    private String CODENAME;
    @Column
    private String STRTDATE;
    @Column
    private String END_DATE;
    @Column
    private String PLACE;
    @Column
    private String PROGRAM;
    @Column
    private String ORG_LINK;
    @Column
    private String USE_FEE;
    @Column
    private Double LOT;
    @Column
    private Double LAT;
    @Column
    private String PLAYER;

    public EventDTO toDto() {
        return EventDTO.builder()
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
