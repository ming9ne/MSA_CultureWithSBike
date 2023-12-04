package com.sth.eventservice.vo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import javax.xml.transform.Result;
import java.util.List;

@Data
@XmlRootElement(name = "culturalEventInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventResponseTotal {

    @XmlElement(name = "row")
    private List<Eventdata> events;
}