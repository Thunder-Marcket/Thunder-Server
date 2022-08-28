package com.example.demo.src.follows;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follows.model.PostFollowsRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@AllArgsConstructor
@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private FollowProvider followProvider;
    private FollowDao followDao;

    public PostFollowsRes followOrUnFollow(int userIdx, int followingUserIdx) throws BaseException {
        String resultMessage = "";
        if (followProvider.checkFollowing(userIdx, followingUserIdx) == 0) {
            int result = followDao.follow(userIdx, followingUserIdx);
            if (result == 0) {
                throw new BaseException(POST_FAIL_FOLLOWS);
            }
            resultMessage = "팔로우 성공";
        } else {
            if (followProvider.checkFollowStatus(userIdx, followingUserIdx).equals("enable")) {
                int result = followDao.unfollow(userIdx, followingUserIdx);
                if (result == 0) {
                    throw new BaseException(MODIFY_FAIL_FOLLOWS_STATUS);
                }
                resultMessage = "언팔로우 성공";
            } else {
                int result = followDao.modifyFollowStatus(userIdx, followingUserIdx);
                if (result == 0) {
                    throw new BaseException(MODIFY_FAIL_FOLLOWS_STATUS);
                }
                resultMessage = "팔로우 성공";
            }
        }
        PostFollowsRes postFollowsRes = new PostFollowsRes(followingUserIdx, userIdx, resultMessage);
        return postFollowsRes;
    }
}
