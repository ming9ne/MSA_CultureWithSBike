// Citydata.java
package com.sth.eventservice.vo;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Citydata {
    @XmlElementWrapper(name = "EVENT_STTS")
    @XmlElement(name = "EVENT_STTS")
    private List<EventStts> events;
}
