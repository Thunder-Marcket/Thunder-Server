package com.example.demo.src.Orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressRes {
    private int addressIdx;
    private String userName;
    private String address;
    private String detailAddress;
    private String phoneNumber;
}
