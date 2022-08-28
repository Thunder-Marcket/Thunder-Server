package com.example.demo.src.follows.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostFollowsRes {
    private int followingUserIdx;
    private int userIdx;
    private String resultMessage;
}
