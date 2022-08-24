package com.example.demo.src.searchs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetSearchesRes {
    private int userIdx;
    private String searchText;
    private List<String> brandNameList;
}
