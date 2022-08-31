package com.example.demo.src.Advertisements.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMainAdRes {
    private String adUrl;
    private String content;
    private String mainAdImage;
    private int advertisementIdx;
}
