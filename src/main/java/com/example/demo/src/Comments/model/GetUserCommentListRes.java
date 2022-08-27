package com.example.demo.src.Comments.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetUserCommentListRes {
    private int commentCount;
    private List<GetUserCommentRes> userCommentList;
}
