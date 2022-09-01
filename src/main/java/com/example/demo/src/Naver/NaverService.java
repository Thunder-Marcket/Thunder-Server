package com.example.demo.src.Naver;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Naver.model.GetLocationRes;
import com.example.demo.src.Naver.model.PostTransReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class NaverService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final NaverDao naverDao;

    @Autowired
    public NaverService(NaverDao naverDao) {
        this.naverDao = naverDao;
    }


    public void addPoint(PostTransReq postTransReq, GetLocationRes getLocationRes, int userIdx) throws BaseException {
        if(naverDao.checkAddress(userIdx, postTransReq) == 0){
            throw new BaseException(INVALID_USER_JWT);
        }

        try{
            naverDao.addPoint(postTransReq, getLocationRes);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
