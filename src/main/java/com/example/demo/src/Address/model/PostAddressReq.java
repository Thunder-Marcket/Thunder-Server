package com.example.demo.src.Address.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAddressReq {
    private String userName;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private int isBaseAddress;
    private int userIdx;
}
