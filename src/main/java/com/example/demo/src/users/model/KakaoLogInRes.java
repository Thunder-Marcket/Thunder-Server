package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLogInRes  {
    private int userIdx;
    private String jwt;
}
