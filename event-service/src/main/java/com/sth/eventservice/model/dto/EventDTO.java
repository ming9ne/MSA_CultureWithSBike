package com.sth.eventservice.model.dto;

import com.sth.eventservice.model.entity.Event;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    @XmlElement(nillable = true)
    private String areaNm;
    @XmlElement(nillable = true)
    private String eventNm;
    @XmlElement(nillable = true)
    private String GUNAME;
    @XmlElement(nillable = true)
    private String TITLE;
    @XmlElement(nillable = true)
    private String CODENAME;
    @XmlElement(nillable = true)
    private String STRTDATE;
    @XmlElement(nillable = true)
    private String END_DATE;
    @XmlElement(nillable = true)
    private String PLACE;
    @XmlElement(nillable = true)
    private String PROGRAM;
    @XmlElement(nillable = true)
    private String ORG_LINK;
    @XmlElement(nillable = true)
    private Double LOT;
    @XmlElement(nillable = true)
    private Double LAT;

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
                .build();
    }

    public String getTitle() {
        return TITLE;
    }
}
