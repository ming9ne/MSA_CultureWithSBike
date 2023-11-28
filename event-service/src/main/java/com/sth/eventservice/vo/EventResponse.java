package com.sth.eventservice.vo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "SeoulRtd.citydata")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventResponse {
    @XmlElement(name = "CITYDATA")
    private Citydata citydata;
}
