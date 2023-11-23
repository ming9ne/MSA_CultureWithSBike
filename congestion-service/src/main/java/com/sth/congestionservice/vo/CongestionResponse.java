package com.sth.congestionservice.vo;

import com.sth.congestionservice.model.dto.CongestionDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@XmlRootElement(name = "Map")
public class CongestionResponse {
    private List<Citydata> row;

    @XmlElement(name = "SeoulRtd.citydata_ppltn")
    public void setRow(List<Citydata> row) {
        this.row = row;
    }
}
