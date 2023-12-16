package com.sth.sbikeservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEvent {
    private Long eventId;
    private String eventNm;
    private double lot;
    private double lat;
}
