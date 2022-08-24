package com.example.demo.src.likes;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.likes.model.GetLikeItemRes;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@AllArgsConstructor
@RestController
@RequestMapping("/likes")
public class LikeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LikeProvider likeProvider;
    @Autowired
    private final LikeService likeService;
    @Autowired
    private final JwtService jwtService;

    /**
     * 찜 목록 조회 API
     * [GET] /likes/:userIdx
     * @return Baseresponse<List<GetLikeItemRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetLikeItemRes>> getLikeItems(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetLikeItemRes> getLikeItemResList = likeProvider.getLikeItems(userIdx);
            return new BaseResponse<>(getLikeItemResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
