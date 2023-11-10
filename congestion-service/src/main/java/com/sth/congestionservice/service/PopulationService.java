package com.sth.congestionservice.service;

import com.sth.congestionservice.repository.PopulationRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PopulationService {
    private PopulationRepositoy populationRepositoy;

    @Autowired
    public PopulationService(PopulationRepositoy populationRepositoy) {
        this.populationRepositoy = populationRepositoy;
    }
}
