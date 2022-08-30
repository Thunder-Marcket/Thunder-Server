package com.example.demo.src.searches.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetSearchUserRes {
    private int userIdx;
    private String profileImgUrl;
    private String userName;
    private int followerCnt;
    private int itemCnt;
}
