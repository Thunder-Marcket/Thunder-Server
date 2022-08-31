package com.example.demo.src.categories.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryItemsRes {
    private CategoryInfo category;
    private List<CategoryInfo> categoryInfoList;
    private List<ItemInfo> itemInfoList;
}
