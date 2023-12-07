package com.sth.eventservice.model.entity;

import com.sth.eventservice.model.dto.EventDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
public class Event {
    @Id
    private String eventNm;
    @Column
    private String areaNm;
    @Column
    private String guname;
    @Column
    private String title;
    @Column
    private String codename;
    @Column
    private LocalDate strtdate;
    @Column
    private LocalDate endDate;
    @Column
    private String place;
    @Column
    private String program;
    @Column
    private String useFee;
    @Column
    private String orgLink;
    @Column
    private Double lot;
    @Column
    private Double lat;
    @Column
    private String player;
    @Column
    private String mainImg;


    public EventDTO toDto() {
        return EventDTO.builder()
                .eventNm(eventNm)
                .areaNm(areaNm)
                .guname(guname)
                .codename(codename)
                .strtdate(strtdate)
                .endDate(endDate)
                .title(title)
                .place(place)
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