package com.example.demo.src.Orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetIndirectOrderRes {
    private GetAddressRes address;
    private String itemName;
    private String itemImageUrl;
    private int point;
    private String itemCost;
    private String safePayCost;
    private String isIncludeOrderTip;
    private String totalCost;
}
