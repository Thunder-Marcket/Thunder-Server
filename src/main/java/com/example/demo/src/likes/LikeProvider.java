package com.example.demo.src.likes;

import com.example.demo.config.BaseException;
import com.example.demo.src.likes.model.GetLikeItemRes;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@AllArgsConstructor
@Service
public class LikeProvider {
    private final LikeDao likeDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public List<GetLikeItemRes> getLikeItems(int userIdx) throws BaseException {
        try {
            List<GetLikeItemRes> getLikeItemResList = likeDao.getLikeItems(userIdx);
            return getLikeItemResList;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
