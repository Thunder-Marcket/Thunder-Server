package com.example.demo.src.Brands.model;

import com.example.demo.src.Items.model.GetItemListRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowBrandRes {
    private GetBrandListRes brandRes;
    private List<GetItemListRes> brandItems;
}
