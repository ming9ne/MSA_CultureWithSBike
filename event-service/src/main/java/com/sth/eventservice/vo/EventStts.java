package com.sth.eventservice.vo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class EventStts {
    //    @XmlElement(name = "EVENT_STTS")
//    private EventData eventData;
    private String EVENT_NM;
    private String AREA_NM;
    private String GUNAME;
    private String TITLE;
    private String CODENAME;
    private String STRTDATE;
    private String END_DATE;
    private String PLACE;
    private String PROGRAM;
    private String ORG_LINK;
    private Double LOT;
    private Double LAT;
    private String USE_FEE;
    private String PLAYER;


}