package com.example.demo.src.address.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressRes {
    private String userName;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private int isBaseAddress;
}
