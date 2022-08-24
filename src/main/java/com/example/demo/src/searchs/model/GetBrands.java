package com.example.demo.src.searchs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetBrands {
    private String brandName;
    private String brandSubName;
    private String imageUrl;
    private int isFollowing;
}
