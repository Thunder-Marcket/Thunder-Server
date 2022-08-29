package com.example.demo.src.salesViews;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.salesViews.model.GetSalesViewsRes;
import com.example.demo.src.salesViews.model.PatchSalesViewsReq;
import com.example.demo.src.salesViews.model.PatchSalesViewsRes;
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
@RequestMapping("/sales-views")
public class SalesViewsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SalesViewsProvider salesViewsProvider;
    @Autowired
    private final SalesViewsService salesViewsService;
    @Autowired
    private final JwtService jwtService;

    /**
     * 최근 본 상품 조회 API
     * [GET] /sales-views/:userIdx
     * @return BaseResponse<List < GetSalesViewsRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetSalesViewsRes>> getSalesViews(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetSalesViewsRes> getSalesViewsResList = salesViewsProvider.getSalesViews(userIdx);
            return new BaseResponse<>(getSalesViewsResList);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 최근 본 상품 삭제
     * [PATCH] /sales-views/d/:userIdx
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/d/{userIdx}")
    public BaseResponse<PatchSalesViewsRes> modifySalesViewsStatus(@PathVariable("userIdx") int userIdx,
                                                                   @RequestBody PatchSalesViewsReq patchSalesViewsReq) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchSalesViewsRes patchSalesViewsRes = salesViewsService.modifySalesViewsStatus(patchSalesViewsReq);
            return new BaseResponse<>(patchSalesViewsRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
