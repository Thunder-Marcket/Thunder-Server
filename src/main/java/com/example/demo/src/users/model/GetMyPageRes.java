package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetMyPageRes {
    private GetUserRes getUserRes;
    private List<GetUserItemRes> getUserItemResList;
}
