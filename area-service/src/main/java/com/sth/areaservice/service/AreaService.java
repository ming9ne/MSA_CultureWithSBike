package com.sth.areaservice.service;

import com.sth.areaservice.model.dto.AreaDTO;
import com.sth.areaservice.model.entity.Area;
import com.sth.areaservice.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaService {
    private final AreaRepository areaRepository;

    @Autowired
    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<AreaDTO> listArea() {
        List<Area> list = new ArrayList<>();
        List<AreaDTO> resultList = new ArrayList<>();

        list = areaRepository.findAll();
        for(Area area : list) {
            resultList.add(area.toDto());
        }

        return resultList;
    }
}
