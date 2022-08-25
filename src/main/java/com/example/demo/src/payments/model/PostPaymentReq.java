package com.example.demo.src.payments.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostPaymentReq {
    private String accountName;
    private int monthlyPlan;
}
