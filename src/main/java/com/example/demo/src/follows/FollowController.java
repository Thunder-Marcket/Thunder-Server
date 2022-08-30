package com.example.demo.src.follows;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follows.model.GetFollowsRes;
import com.example.demo.src.follows.model.PostFollowsRes;
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
@RequestMapping("/follows")
public class FollowController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowService followService;
    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final JwtService jwtService;

    /**
     * 팔로우/언팔로우 API
     * [POST] /follows/:userIdx/:followingUserIdx
     *
     * @return BaseResponse<PostFollowsRes>
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{followingUserIdx}")
    public BaseResponse<PostFollowsRes> followOrUnFollow(@PathVariable("userIdx") int userIdx,
                                                 @PathVariable("followingUserIdx") int followingUserIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostFollowsRes postFollowsRes = followService.followOrUnFollow(userIdx, followingUserIdx);
            return new BaseResponse<>(postFollowsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내 팔로잉 조회 API
     * [GET] /follows/:userIdx
     *
     * @return BaseResponse<GetFollowsRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetFollowsRes>> getFollowings(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetFollowsRes> getFollowsResList = followProvider.getFollowings(userIdx);
            return new BaseResponse<>(getFollowsResList);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
