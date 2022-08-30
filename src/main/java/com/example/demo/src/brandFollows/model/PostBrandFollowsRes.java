package com.example.demo.src.brandFollows.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostBrandFollowsRes {
    private int userIdx;
    private int brandIdx;
    private String resultMessage;
}
