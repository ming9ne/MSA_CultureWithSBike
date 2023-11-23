package com.sth.eventservice.vo;

import com.sth.eventservice.model.dto.EventDTO;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "culturalEventInfo")
public class Greeting {
    private List<EventDTO> row;

    @XmlElement(name = "row")
    public void setRow(List<EventDTO> row) {
        this.row = row;
    }

    public List<EventDTO> getRow() {
        return row;
    }
}
