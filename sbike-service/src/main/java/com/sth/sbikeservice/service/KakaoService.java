package com.sth.sbikeservice.service;

import com.sth.sbikeservice.model.dto.KakoDTO;
import com.sth.sbikeservice.model.dto.SbikeDTO;
import com.sth.sbikeservice.model.entity.KaKao;
import com.sth.sbikeservice.model.entity.Sbike;
import com.sth.sbikeservice.repository.KaKaoRepository;
import com.sth.sbikeservice.repository.SbikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KakaoService {

    private final KaKaoRepository kaKaoRepository;

    @Autowired
    public KakaoService(KaKaoRepository kaKaoRepository) {
        this.kaKaoRepository = kaKaoRepository;
    }

    public List<KakoDTO> listKakao() {
        List<KaKao> list = kaKaoRepository.findAll();
        List<KakoDTO> resultList = new ArrayList<>();
        list.forEach(kaKao -> resultList.add(kaKao.toDto()));
        return resultList;
    }




}