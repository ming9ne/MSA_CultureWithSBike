package com.sth.eventservice.model.dto;

import com.sth.eventservice.model.entity.Event;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EventDTO {
    private Long eventId;
    private String eventNm;
    private String areaNm;
    private String guname;
    private String title;
    private String codename;
    private LocalDate strtdate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();
    private String place;
    private String program;
    private String useFee;
    private String orgLink;
    private Double lot;
    private Double lat;
    private String player;
    private String mainImg;

    public Event toEntity() {
        return Event.builder()
                .eventId(eventId)
                .eventNm(eventNm)
                .areaNm(areaNm)
                .guname(guname)
                .codename(codename)
                .strtdate(strtdate != null ? LocalDate.parse(strtdate.toString()) : null)  // null 체크 추가
                .endDate(endDate != null ? LocalDate.parse(endDate.toString()) : null)    // null 체크 추가
                .place(place)
                .title(title)
                .program(program)
                .useFee(useFee)
                .orgLink(orgLink)
                .lot(lot)
                .lat(lat)
                .player(player)
                .mainImg(mainImg)
                .build();
    }
}