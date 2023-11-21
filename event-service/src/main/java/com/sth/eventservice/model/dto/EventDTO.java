package com.sth.eventservice.model.dto;

import com.sth.eventservice.model.entity.Event;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private UUID id;
    private String title; // 공연 행사명
    private String strtdate; // 공연 시작 시간
    private String end_date; // 공연 종료 시간

    private String codename; // 분류
    private String guname; // 자치구
    private String place; // 장소
    private String useFee; // 이용 요금
    private String player; // 출연자 정보
    private String program; // 프로그램 소개
    private String org_link; // 홈페이지 주소
    private double lot; // 위도 X좌표
    private double lat; // 경도 Y좌표

    public Event toEntity() {
        return Event.builder()
                .id(id !=null ? id: UUID.randomUUID())
                .title(title)
                .strtdate(strtdate)
                .end_date(end_date)
                .codename(codename)
                .guname(guname)
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
