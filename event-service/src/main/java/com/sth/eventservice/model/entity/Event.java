package com.sth.eventservice.model.entity;


import com.sth.eventservice.model.dto.EventDTO;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    private String guname; // 자치구
    @Column
    private String title; // 공연 행사명
    @Column
    private String codename; // 분류
    @Column
    private String strtdate; // 날짜 시간
    @Column
    private String end_date;
    @Column
    private String place; // 장소
    @Column
    private String useFee; // 이용 요금
    @Column
    private String player; // 출연자 정보
    @Column
    private String program; // 프로그램 소개
    @Column
    private String org_link; // 홈페이지 주소
    @Column
    private double lot; // 위도 X좌표
    @Column
    private double lat; // 경도 Y좌표

    public EventDTO toDto(){
        return EventDTO.builder()
                .guname(guname)
                .title(title)
                .codename(codename)
                .strtdate(strtdate)
                .end_date(end_date)
                .place(place)
                .useFee(useFee)
                .player(player)
                .program(program)
                .org_link(org_link)
                .lot(lot)
                .lat(lat)
                .build();
    }
}
