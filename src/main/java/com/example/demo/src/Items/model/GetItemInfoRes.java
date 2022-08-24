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
    private List<String> tagList;
    //private List<GetItemListRes> storeItemList;
    private List<GetItemListRes> similarItemList;
    private List<Comments> commentList;
    private String cost;
    private String itemName;
    private String address;
    private String period;
    private int isLike;
    private int likeCount;
    private String itemCount;
    private int isCanExchange;
    private int isIncludeOrderTip;
    private int isUsed;
    private String itemContent;
    private int viewCount;
    private int chatCount;
    private int userIdx;
    private int followingCount;
    private int isFollowCheck;
    private float star;
    private int storeItemCount;
    private String storeImageUrl;
    private int isCanCheck;
    private int isSafePayment;
    private int commentCount;


}
