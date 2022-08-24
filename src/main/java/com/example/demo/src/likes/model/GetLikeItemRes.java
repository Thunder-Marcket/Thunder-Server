package com.example.demo.src.likes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetLikeItemRes {
    private int itemIdx;
    private String itemName;
    private int cost;
    private String userName;
    private String uploadTime;
    private String imageUrl;
}
