package com.example.demo.src.brands.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBrandListRes {
    private int brandIdx;
    private String brandName;
    private String brandSubName;
    private String brandItemCount;
    private int isFollowCheck;
    private String storeImageUrl;
}
