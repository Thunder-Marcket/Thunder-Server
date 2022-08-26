package com.example.demo.src.Orders.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPurchaseOrderRes {
    private int orderIdx;
    private String itemName;
    private String itemCost;
    private String itemUrl;
    private String storeName;
    private String orderTime;
}
