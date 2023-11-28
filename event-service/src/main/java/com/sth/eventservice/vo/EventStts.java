package com.sth.eventservice.vo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class EventStts {
//    @XmlElement(name = "EVENT_STTS")
//    private EventData eventData;
    private String EVENT_NM;
    private String EVENT_PERIOD;
    private String EVENT_PLACE;
    private String EVENT_ETC_DETAIL;
    private String EVENT_X;
    private String EVENT_Y;
}
