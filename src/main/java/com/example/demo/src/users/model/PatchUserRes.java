package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserRes {
    private int userIdx;
    private String userName;
    private String profileImgUrl;
    private String status;
}
