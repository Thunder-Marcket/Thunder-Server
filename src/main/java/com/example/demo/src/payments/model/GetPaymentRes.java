package com.example.demo.src.payments.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetPaymentRes {
    private int paymentIdx;
    private int accountIdx;
    private String accountName;
    private int monthlyPlan;
}
