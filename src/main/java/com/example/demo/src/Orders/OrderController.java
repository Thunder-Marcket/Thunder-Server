package com.example.demo.src.Orders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Orders.model.GetDirectOrderRes;
import com.example.demo.src.Orders.model.GetIndirectOrderRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderProvider orderProvider;

    private final OrderService orderService;

    private final JwtService jwtService;

    @Autowired
    public OrderController(OrderProvider orderProvider, OrderService orderService, JwtService jwtService) {
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    /**
     * 직거래 주문 페이지 조회 API
     * [GET] /orders/direct/:itemIdx/:use-point
     * @return BaseResponse<GetDirectOrderRes>
     */
    @ResponseBody
    @GetMapping("/direct/{itemIdx}/{use-point}")
    public BaseResponse<GetDirectOrderRes> getDirectOrderPage(@PathVariable("itemIdx") int itemIdx,
                                                              @PathVariable("use-point") int usePoint){
        try{
            int userIdx = jwtService.getUserIdx();

            GetDirectOrderRes getDirectOrderRes = orderProvider.getDirectOrderPage(userIdx, itemIdx, usePoint);
            return new BaseResponse<>(getDirectOrderRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 택배 거래 주문 페이지 조회 API
     * [GET] /orders/indirect/:itemIdx/:use-point
     * @return BaseResponse<GetDirectOrderRes>
     */
    @ResponseBody
    @GetMapping("/indirect/{itemIdx}/{use-point}")
    public BaseResponse<GetIndirectOrderRes> getIndirectOrderPage(@PathVariable("itemIdx") int itemIdx,
                                                              @PathVariable("use-point") int usePoint){
        try{
            int userIdx = jwtService.getUserIdx();

            GetIndirectOrderRes getIndirectOrderRes = orderProvider.getIndirectOrderPage(userIdx, itemIdx, usePoint);
            return new BaseResponse<>(getIndirectOrderRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
