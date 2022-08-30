package com.example.demo.src.follows;

import com.example.demo.config.BaseException;
import com.example.demo.src.follows.model.Following;
import com.example.demo.src.follows.model.FollowingUserItem;
import com.example.demo.src.follows.model.GetFollowerRes;
import com.example.demo.src.follows.model.GetFollowingRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<GetFollowingRes> getFollowings(int userIdx) throws BaseException {
        try {
            List<GetFollowingRes> getFollowsResList = new ArrayList<>();
            List<Following> followingList = followDao.getFollowings(userIdx);
            for(Following following : followingList) {
                List<FollowingUserItem> followingUserItemList = followDao.getFollowingUserItemList(following.getUserIdx());
                getFollowsResList.add(new GetFollowingRes(following, followingUserItemList));
            }
            return getFollowsResList;
        }catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowerRes> getFollowers(int userIdx) throws BaseException {
        try {
            List<GetFollowerRes> getFollowerResList = followDao.getFollowers(userIdx);
            return getFollowerResList;
        }catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
