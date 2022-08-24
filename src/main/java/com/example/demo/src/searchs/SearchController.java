package com.example.demo.src.searchs;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.searchs.model.GetSearchesRes;
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
    private final JwtService jwtService;

    /**
     * 최근 검색어 조회 API
     * [GET] /searches/:userIdx
     *
     * @return BaseResponse<List < GetSearchesRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetSearchesRes>> getSearches(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetSearchesRes> getSearchesResList = searchProvider.getSearches(userIdx);
            return new BaseResponse<>(getSearchesResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
