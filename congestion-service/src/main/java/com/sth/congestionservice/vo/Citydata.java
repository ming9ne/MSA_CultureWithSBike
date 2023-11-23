package com.sth.congestionservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Citydata {
    private String AREA_NM;
    private String AREA_CONGEST_LVL;
    private String AREA_CONGEST_MSG;
    private int AREA_PPLTN_MIN;
    private int AREA_PPLTN_MAX;
}
