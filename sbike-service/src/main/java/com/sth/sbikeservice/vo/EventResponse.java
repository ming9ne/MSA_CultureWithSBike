package com.sth.sbikeservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private double lot;
    private double lat;
    private String eventNm;
    private String stationName;
    private Long eventId;
}
