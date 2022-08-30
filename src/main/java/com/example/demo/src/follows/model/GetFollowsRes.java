package com.example.demo.src.follows.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetFollowsRes {
    private Following following;
    private List<FollowingUserItem> followingUserItemList;
}
