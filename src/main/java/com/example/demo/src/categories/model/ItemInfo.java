package com.example.demo.src.categories.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfo {
    private int itemIdx;
    private String imageUrl;
    private String cost;
    private String itemName;
    private int isSafePayment;
    private int isLike;
    private int isAdItem;
    private String uploadTime;
    private String address;
    private int likeCnt;
}
