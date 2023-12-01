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
    private String eventNm;
    private String areaNm;
    private String guname;
    private String title;
    private String codename;
    private String strtdate;
    private String endDate;
    private String place;
    private String program;
    private String useFee;
    private String orgLink;
    private Double lot;
    private Double lat;
    private String player;

    public Event toEntity() {
        return Event.builder()
            .eventNm(eventNm)
            .areaNm(areaNm)
            .guname(guname)
            .codename(codename)
            .strtdate(strtdate)
            .endDate(endDate)
            .place(place)
            .program(program)
            .useFee(useFee)
            .orgLink(orgLink)
            .lot(lot)
            .lat(lat)
            .player(player)
            .build();
    }
}
