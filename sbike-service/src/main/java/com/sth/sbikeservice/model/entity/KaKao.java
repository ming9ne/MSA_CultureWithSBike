// KaKao.java
package com.sth.sbikeservice.model.entity;

import com.sth.sbikeservice.model.dto.KakoDTO;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.vo.NearSbike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KaKao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Long eventId;

    @Column(length = 1000)
    private String eventName;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Sbike> sbike = new ArrayList<>();


    public KakoDTO toDto() {
        return KakoDTO.builder()
                .id(id)
                .eventId(eventId)
                .eventName(eventName)
                .sbike(sbike)
                .build();
    }

}