package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserItemRes {
    private int itemIdx;
    private String itemName;
    private String imageUrl;
    private String cost;
    private int isSafePayment;
    private String uploadTime;
    private int itemCnt;
}
