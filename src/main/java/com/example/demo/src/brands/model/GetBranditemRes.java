package com.example.demo.src.brands.model;

import com.example.demo.src.Items.model.GetItemListRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBranditemRes {
    private GetBrandListRes brandRes;
    private GetItemListRes brandItems;
}
