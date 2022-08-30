package com.example.demo.src.follows.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetFollowerRes {
    private int userIdx;
    private String profileImgUrl;
    private String userName;
    private int itemCnt;
    private int followerCnt;
}
