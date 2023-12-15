package com.sth.sbikeservice.service;

import com.sth.sbikeservice.model.dto.KakoDTO;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.KaKaoRepository;
import com.sth.sbikeservice.repository.SbikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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


}