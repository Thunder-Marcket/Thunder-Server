package com.example.demo.src.Items.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostItemReq {
    private List<String> tagNameList;
    private List<String> imageUrlList;

    private int categoryIdx;
    private int subCategoryIdx;
    private int subSubcategoryIdx;

    private String itemName;
    private String address;
    private String itemContent;
    private int itemCost;
    private int itemCount;
    private int userIdx;
    private int isIncludeOrderTip;
    private int isSafePayment;
    private int isUsed;
    private int isCanExchange;
}
