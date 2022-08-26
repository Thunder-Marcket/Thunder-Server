package com.example.demo.src.Orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostOrderReq {
    private int addressIdx;
    private int itemIdx;
    private int buyUserIdx;
    private String orderRequest;
    private int isDirectDeal;
    private int paymentIdx;
}
