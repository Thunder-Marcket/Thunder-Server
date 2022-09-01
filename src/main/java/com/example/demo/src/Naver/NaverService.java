package com.example.demo.src.Naver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaverService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final NaverDao naverDao;

    @Autowired
    public NaverService(NaverDao naverDao) {
        this.naverDao = naverDao;
    }


}
