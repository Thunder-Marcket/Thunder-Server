package com.example.demo.src.reports.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostReportReq {
    private String reportType;
    private String detailType;
    private String sellUserName;
    private String message;
    private List<String> imageUrlList;
}
