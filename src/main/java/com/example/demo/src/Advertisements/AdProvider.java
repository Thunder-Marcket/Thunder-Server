package com.example.demo.src.Advertisements;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Advertisements.model.GetMainAdRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AdProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AdDao adDao;

    @Autowired
    public AdProvider(AdDao adDao) {
        this.adDao = adDao;
    }

    public List<GetMainAdRes> getMainAd() throws BaseException {
        try{
            List<GetMainAdRes> getMainAdRes = adDao.getMainAd();
            return getMainAdRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
