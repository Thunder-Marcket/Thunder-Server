package com.example.demo.src.searches.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetSearchesRes {
    private List<GetSearch> searchList;
    private List<GetBrands> brandNameList;
}
