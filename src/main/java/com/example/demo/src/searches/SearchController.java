package com.example.demo.src.searches;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.searches.model.*;
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
@RequestMapping("/searches")
public class SearchController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;
    @Autowired
    private final JwtService jwtService;

    /**
     * 최근 검색어 조회 API
     * [GET] /searches/:userIdx
     *
     * @return BaseResponse<List < GetSearchesRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetSearchesRes> getSearches(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetSearch> getSearches = searchProvider.getSearches(userIdx);
            List<GetBrands> getBrands = searchProvider.getBrands(userIdx);
            GetSearchesRes getSearchesRes = new GetSearchesRes(getSearches, getBrands);
            return new BaseResponse<>(getSearchesRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 최근 검색어 삭제하기 (1개)
     * [PATCH] /searches/d/:userIdx
     *
     * @return BaseResponse<PatchSearchesRes>
     */
    @ResponseBody
    @PatchMapping("/d/{userIdx}")
    public BaseResponse<GetSearchesRes> modifySearchStatus(@PathVariable("userIdx") int userIdx,
                                                             @RequestBody PatchSearchesReq patchSearchesReq) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetSearchesRes getSearchesRes = searchService.modifySearchStatus(userIdx, patchSearchesReq);
            return new BaseResponse<>(getSearchesRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 최근 검색어 삭제하기 (전체)
     * [PATCH] /searches/all-d/:userIdx
     *
     * @return BaseResponse<PatchSearchesRes>
     */
    @ResponseBody
    @PatchMapping("/all-d/{userIdx}")
    public BaseResponse<GetSearchesRes> modifyAllSearchStatus(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetSearchesRes getSearchesRes = searchService.modifyAllSearchStatus(userIdx);
            return new BaseResponse<>(getSearchesRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
