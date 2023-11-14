package com.sth.eventservice.model.dto;

import com.sth.eventservice.model.entity.EventEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private String codename; // 분류
    private String guname; // 자치구
    private String title; // 공연 행사명
    private String date; // 날짜 시간
    private String place; // 장소
    private String useFee;  // 이용 요금
    private String player; // 출연자 정보
    private String program; // 프로그램 소개
    private double lot; // 위도 X좌표
    private double lat; // 경도 Y좌표



    public EventEntity toEntity() {
        return EventEntity.builder()
                .codename(codename)
                .guname(guname)
                .title(title)
                .date(date)
                .place(place)
                .useFee(useFee)
                .player(player)
                .program(program)
                .lot(lot)
                .lat(lat)
                .build();
    }
}
