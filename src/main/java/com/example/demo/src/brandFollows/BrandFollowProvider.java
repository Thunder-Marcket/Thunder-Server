package com.example.demo.src.brandFollows;

import com.example.demo.config.BaseException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@AllArgsConstructor
@Service
public class BrandFollowProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BrandFollowDao brandFollowDao;

    public int checkBrandFollows(int userIdx, int brandIdx) throws BaseException {
        try {
            return brandFollowDao.checkBrandFollows(userIdx, brandIdx);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String checkBrandFollowsStatus(int userIdx, int brandIdx) throws BaseException {
        try {
            return brandFollowDao.checkBrandFollowsStatus(userIdx, brandIdx);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
