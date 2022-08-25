package com.example.demo.src.payments;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payments.model.*;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@AllArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PaymentService paymentService;
    @Autowired
    private final PaymentProvider paymentProvider;
    @Autowired
    private final JwtService jwtService;

    /**
     * 결제수단 등록 API
     * [POST] /payments/:userIdx
     *
     * @return BaseResponse<PostPaymentRes>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostPaymentRes> createPayment(@PathVariable("userIdx") int userIdx,
                                                      @RequestBody PostPaymentReq postPaymentReq) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostPaymentRes postPaymentRes = paymentService.createPayment(userIdx, postPaymentReq);
            return new BaseResponse<>(postPaymentRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 결제수단 조회 API
     * [GET] /payments/:userIdx
     *
     * @return BaseResponse<GetPaymentRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetPaymentRes> getPayment(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetPaymentRes getPaymentRes = paymentProvider.getPayment(userIdx);
            return new BaseResponse<>(getPaymentRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 할부 설정 API
     * [PATCH] /payments/:userIdx/monthly-plan
     *
     * @return BaseResponse<GetPaymentRes>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/monthly-plan")
    public BaseResponse<GetPaymentRes> modifyPaymentMonthlyPlan(@PathVariable("userIdx") int userIdx,
                                                                @RequestBody PatchPaymentMonthlyPlanReq patchPaymentMonthlyPlanReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetPaymentRes getPaymentRes = paymentService.modifyPaymentMonthlyPlan(userIdx, patchPaymentMonthlyPlanReq);
            return new BaseResponse<>(getPaymentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 결제수단 수정 API
     * [PATCH] /payments/:userIdx
     *
     * @return BaseResponse<GetPaymentRes>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<GetPaymentRes> modifyPayment(@PathVariable("userIdx") int userIdx,
                                                     @RequestBody PatchPaymentReq patchPaymentReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetPaymentRes getPaymentRes = paymentService.modifyPayment(userIdx, patchPaymentReq);
            return new BaseResponse<>(getPaymentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
