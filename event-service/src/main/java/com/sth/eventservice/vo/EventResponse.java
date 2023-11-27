package com.sth.eventservice.vo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "SeoulRtd.citydata")
public class EventResponse {
    private List<Citydata> row;

    @XmlElement(name = "CITYDATA")
    public void setRow(List<Citydata> row) {
        this.row = row;
    }

    @Data
    public static class Citydata {
        private String AREA_NM;
        private List<EventStts> EVENT_STTS;

        @XmlElement(name = "EVENT_STTS")
        public void setEventStts(List<EventStts> eventStts) {
            this.EVENT_STTS = eventStts;
        }
    }

    @Data
    public static class EventStts {
        private String EVENT_NM;

        @XmlElement(name = "EVENT_NM")
        public void setEventNm(String eventNm) {
            this.EVENT_NM = eventNm;
        }
    }
}
