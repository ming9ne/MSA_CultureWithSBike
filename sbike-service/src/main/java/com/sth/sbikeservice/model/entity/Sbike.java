package com.sth.sbikeservice.model.entity;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sbike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID ID;

    private String CODENAME;
    private String GUNAME;
    private String TITLE;
    private String STRTDATE;
    private String END_DATE;
    private String PLACE;
    private String USE_FEE;
    private String PLAYER;
    private String PROGRAM;
    private String ORG_LINK;
    private double LOT;
    private double LAT;


    public SbikeDTO toDto() {
        return SbikeDTO.builder()
                .CODENAME(CODENAME)
                .GUNAME(GUNAME)
                .TITLE(TITLE)
                .STRTDATE(STRTDATE)
                .END_DATE(END_DATE)
                .PLACE(PLACE)
                .USE_FEE(USE_FEE)
                .PLAYER(PLAYER)
                .PROGRAM(PROGRAM)
                .ORG_LINK(ORG_LINK)
                .LOT(LOT)
                .LAT(LAT)
                .build();
    }
}