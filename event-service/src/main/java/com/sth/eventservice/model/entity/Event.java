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
    @XmlElement(nillable = true)
    private String areaNm;
    @Column
    @XmlElement(nillable = true)
    private String eventNm;
    @Column
    @XmlElement(nillable = true)
    private String GUNAME;
    @Column
    @XmlElement(nillable = true)
    private String TITLE;
    @Column
    @XmlElement(nillable = true)
    private String CODENAME;
    @Column
    @XmlElement(nillable = true)
    private String STRTDATE;
    @Column
    @XmlElement(nillable = true)
    private String END_DATE;
    @Column
    @XmlElement(nillable = true)
    private String PLACE;
    @Column
    @XmlElement(nillable = true)
    private String PROGRAM;
    @Column
    @XmlElement(nillable = true)
    private String ORG_LINK;
    @Column
    @XmlElement(nillable = true)
    private Double LOT;
    @Column
    @XmlElement(nillable = true)
    private Double LAT;

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
                .build();
    }
    public String getTitle() {
        return TITLE;
    }
}
