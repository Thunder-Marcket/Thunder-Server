package com.example.demo.src.Items.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetItemInfoRes {
    private List<String> imageUrlList;
    private int cost;
    private String itemName;
    private String address;
    private String period;
    private int isLike;
    private int likeCnt;


}
