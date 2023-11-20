package com.sth.congestionservice.service;

import com.sth.congestionservice.model.dto.CongestionDTO;
import com.sth.congestionservice.model.entity.Congestion;
import com.sth.congestionservice.repository.CongestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CongestionService {
    private final CongestionRepository congestionRepository;

    @Autowired
    public CongestionService(CongestionRepository congestionRepository) {
        this.congestionRepository = congestionRepository;
    }

    // 혼잡도 조회
    public List<CongestionDTO> listCongestion() {
        List<Congestion> list = new ArrayList<>();
        List<CongestionDTO> resultList = new ArrayList<>();

        list = congestionRepository.findAll();
        for(Congestion congestion : list) {
            resultList.add(congestion.toDto());
        }

        return resultList;
    }

    // 혼잡도 추가
    public void addCongestion(CongestionDTO congestionDTO) {
        congestionRepository.save(congestionDTO.toEntity());
    }


}
