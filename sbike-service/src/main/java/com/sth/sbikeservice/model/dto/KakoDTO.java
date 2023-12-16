package com.sth.sbikeservice.model.dto;

import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakoDTO {
    private int id;
    private Long eventId;
    private String eventName;
    private List<Sbike> sbike = new ArrayList<>();


    public KaKao toEntity() {
        return KaKao.builder()
                .id(id)
                .eventId(eventId)
                .eventName(eventName)
                .sbike(sbike)
                .build();
    }

}
