package com.sth.sbikeservice.service;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.SbikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SbikeService {

    @Autowired
    private SbikeRepository sbikeRepository;

    public List<SbikeDTO> listSbike() {
        List<Sbike> list = sbikeRepository.findAll();
        List<SbikeDTO> resultList = new ArrayList<>();
        list.forEach(sbike -> resultList.add(sbike.toDto()));
        return resultList;
    }

    public Optional<Sbike> findSbikeByStationId(String stationId) {

         Optional<Sbike> sbikeDTO = sbikeRepository.findByStationId(stationId);
         return sbikeDTO;
    }
}