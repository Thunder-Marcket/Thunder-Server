package com.example.demo.src.Items.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comments {
    private float star;
    private int buyUserIdx;
    private int isSafePayment;
    private String userName;
    private String period;
    private String commentText;
    private String imageUrl;
}
