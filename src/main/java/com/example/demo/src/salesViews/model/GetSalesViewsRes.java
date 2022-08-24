package com.example.demo.src.salesViews.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetSalesViewsRes {
    private int itemIdx;
    private String itemName;
    private String cost;
    private int isSafePayment;
    private String uploadTime;
    private String imageUrl;
}
