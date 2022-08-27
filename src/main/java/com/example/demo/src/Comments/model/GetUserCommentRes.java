package com.example.demo.src.Comments.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserCommentRes {
    private int commentIdx;
    private int sellUserIdx;
    private float star;
    private String userName;
    private String period;
    private String commentText;
    private String commentImageUrl;
    private int isSafePayment;
}
