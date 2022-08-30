package com.example.demo.src.brandFollows;

import com.example.demo.config.BaseException;
import com.example.demo.src.brandFollows.model.PostBrandFollowsRes;
import com.example.demo.src.likes.model.PostLikeRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_BRAND_FOLLOWS_STATUS;
import static com.example.demo.config.BaseResponseStatus.POST_FAIL_BRAND_FOLLOWS;


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
                throw new BaseException(POST_FAIL_BRAND_FOLLOWS);
            }
            logger.debug("create brand_follows");
            resultMessage = "브랜드 팔로우 성공";
        } else {
            if (brandFollowProvider.checkBrandFollowsStatus(userIdx, brandIdx).equals("disable")) {
                int result = brandFollowDao.modifyBrandFollowsStatus(userIdx, brandIdx, "enable");
                if (result == 0) {
                    throw new BaseException(MODIFY_FAIL_BRAND_FOLLOWS_STATUS);
                }
                logger.debug("modifyBrandFollowsStatus enable");
                resultMessage = "브랜드 팔로우 성공";
            } else {
                int result = brandFollowDao.modifyBrandFollowsStatus(userIdx, brandIdx, "disable");
                if (result == 0) {
                    throw new BaseException(MODIFY_FAIL_BRAND_FOLLOWS_STATUS);
                }
                logger.debug("modifyBrandFollowsStatus disable");
                resultMessage = "브랜드 팔로우 해제 성공";
            }
        }
        PostBrandFollowsRes postBrandFollowsRes = new PostBrandFollowsRes(userIdx, brandIdx, resultMessage);
        return postBrandFollowsRes;
    }
}
