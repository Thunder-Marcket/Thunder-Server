
package com.example.demo.src.Items.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetItemListRes {
    private int itemIdx;
    private String cost;
    private String itemName;
    private String address;
    private String period;
    private String imageUrl;
    private int isSafePayment;
    private int isLike;
    private int likeCnt;
    private int isCanCheck;
    private int isAdCheck;
    //private String status;
}

