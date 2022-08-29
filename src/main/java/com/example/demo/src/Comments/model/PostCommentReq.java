package com.example.demo.src.Comments.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentReq {
    private List<String> commentImageList;

    private float star;
    private int buyUserIdx;
    private String commentText;
}
