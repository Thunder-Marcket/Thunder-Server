
package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private String profileImgUrl;
    private String userName;
    private int grade;
    private int isSelfVerification;
    private int likeCnt;
    private int commentCnt;
    private int followerCnt;
    private int followingCnt;
}

