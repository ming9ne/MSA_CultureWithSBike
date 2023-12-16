package com.sth.sbikeservice.vo;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NearSbike {
    private SbikeDTO sbikeDTO;
    private int distance;
}
