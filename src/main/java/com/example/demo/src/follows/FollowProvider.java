package com.example.demo.src.follows;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@AllArgsConstructor
@Service
public class FollowProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private FollowDao followDao;

    public int checkFollowing(int userIdx, int followingUserIdx) throws BaseException {
        try {
            int result = followDao.checkFollowing(userIdx, followingUserIdx);
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public String checkFollowStatus(int userIdx, int followingUserIdx) throws BaseException {
        try {
            String result = followDao.checkFollowStatus(userIdx, followingUserIdx);
            return result;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
