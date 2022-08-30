package com.example.demo.src.brandFollows;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.brandFollows.model.PostBrandFollowsRes;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@AllArgsConstructor
@RestController
@RequestMapping("/brand-follows")
public class BrandFollowController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BrandFollowService brandFollowService;
    private final JwtService jwtService;

    /**
     * 브랜드 찜하기/찜 해제하기
     * [POST] /brand_follows/:userIdx/:brandIdx
     *
     * @return BaseResponse<PostBrandFollowsRes>
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{brandIdx}")
    public BaseResponse<PostBrandFollowsRes> createBrandFollows(@PathVariable("userIdx") int userIdx,
                                                                @PathVariable("brandIdx") int brandIdx) throws BaseException {
        try {
            if(userIdx != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            logger.debug("createBrandFollows Controller");
            PostBrandFollowsRes postBrandFollowsRes = brandFollowService.createBrandFollows(userIdx, brandIdx);
            return new BaseResponse<>(postBrandFollowsRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
