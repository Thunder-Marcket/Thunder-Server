package com.example.demo.src.Brands.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBrandListRes {
    private String brandName;
    private String brandSubName;
    private String brandItemCount;
    private int isFollowCheck;
    private String storeImageUrl;
}
