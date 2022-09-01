package com.example.demo.src.Naver.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLocationRes {
    private String roadAddress;
    private String jibunAddress;
    private String x;
    private String y;
}
