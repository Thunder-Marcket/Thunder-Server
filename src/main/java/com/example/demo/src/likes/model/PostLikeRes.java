package com.example.demo.src.likes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeRes {
    private int userIdx;
    private int itemIdx;
    private String resultMessage;
}
