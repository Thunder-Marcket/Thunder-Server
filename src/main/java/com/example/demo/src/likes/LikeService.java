package com.example.demo.src.likes;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.likes.model.PostLikeRes;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@AllArgsConstructor
@Service
public class LikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LikeDao likeDao;
    private final LikeProvider likeProvider;
    private final JwtService jwtService;


    public PostLikeRes createLikes(int userIdx, int itemIdx) throws BaseException {
        String resultMessage = "";
        if (likeProvider.checkLikes(userIdx, itemIdx) == 0) {
            int result = likeDao.createLikes(userIdx, itemIdx);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
            resultMessage = "찜 성공";
        } else {
            if (likeProvider.checkLikeStatus(userIdx, itemIdx).equals("disable")) {
                int result = likeDao.modifyLikeStatus(userIdx, itemIdx, "enable");
                if (result == 0) {
                    throw new BaseException(DATABASE_ERROR);
                }
                resultMessage = "찜 성공";
            } else {
                int result = likeDao.modifyLikeStatus(userIdx, itemIdx, "disable");
                if (result == 0) {
                    throw new BaseException(DATABASE_ERROR);
                }
                resultMessage = "찜 해제 성공";
            }
        }
        PostLikeRes postLikeRes = new PostLikeRes(userIdx, itemIdx, resultMessage);
        return postLikeRes;
    }
}
