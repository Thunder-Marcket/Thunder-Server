package com.example.demo.src.brandFollows;

import com.example.demo.config.BaseException;
import com.example.demo.src.brandFollows.model.PostBrandFollowsRes;
import com.example.demo.src.likes.model.PostLikeRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_LIKE_STATUS;

@AllArgsConstructor
@Service
public class BrandFollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BrandFollowProvider brandFollowProvider;
    private final BrandFollowDao brandFollowDao;

    public PostBrandFollowsRes createBrandFollows(int userIdx, int brandIdx) throws BaseException {
        String resultMessage = "";
        if (brandFollowProvider.checkBrandFollows(userIdx, brandIdx) == 0) {
            int result = brandFollowDao.createBrandFollows(userIdx, brandIdx);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
            logger.debug("create brand_follows");
            resultMessage = "브랜드 팔로우 성공";
        } else {
            if (brandFollowProvider.checkBrandFollowsStatus(userIdx, brandIdx).equals("disable")) {
                int result = brandFollowDao.modifyBrandFollowsStatus(userIdx, brandIdx, "enable");
                if (result == 0) {
                    throw new BaseException(DATABASE_ERROR);
                }
                logger.debug("modifyBrandFollowsStatus enable");
                resultMessage = "브랜드 팔로우 성공";
            } else {
                int result = brandFollowDao.modifyBrandFollowsStatus(userIdx, brandIdx, "disable");
                if (result == 0) {
                    throw new BaseException(DATABASE_ERROR);
                }
                logger.debug("modifyBrandFollowsStatus disable");
                resultMessage = "브랜드 팔로우 해제 성공";
            }
        }
        PostBrandFollowsRes postBrandFollowsRes = new PostBrandFollowsRes(userIdx, brandIdx, resultMessage);
        return postBrandFollowsRes;
    }
}
