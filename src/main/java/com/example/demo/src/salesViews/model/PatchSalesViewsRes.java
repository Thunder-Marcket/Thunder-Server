package com.example.demo.src.salesViews.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchSalesViewsRes {
    private List<SalesViews> viewItemIdxList;
}
