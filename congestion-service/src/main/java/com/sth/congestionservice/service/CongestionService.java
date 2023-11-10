package com.sth.congestionservice.service;

import com.sth.congestionservice.repository.CongestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CongestionService {
    private CongestionRepository congestionRepository;

    @Autowired
    public CongestionService(CongestionRepository congestionRepository) {
        this.congestionRepository = congestionRepository;
    }
}
