package com.example.demo.src.reports;

import com.example.demo.src.reports.model.PostReportReq;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class ReportDao {
    private JdbcTemplate jdbcTemplate;

    public int createReport(int userIdx, PostReportReq postReportReq) {
        String createQuery = "insert into Reports( reportType, detailType, sellUserName, message, userIdx) values (?, ?, ?, ?, ?);";
        Object[] createParams = new Object[]{   postReportReq.getReportType(),
                                                postReportReq.getDetailType(),
                                                postReportReq.getSellUserName(),
                                                postReportReq.getMessage(),
                                                userIdx};
        this.jdbcTemplate.update(createQuery, createParams);
        String lastInsertIdQuery = "select last_insert_id()";
        int reportIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        createReportImage(reportIdx, postReportReq.getImageUrlList());
        return reportIdx;
    }

    private void createReportImage(int reportIdx, List<String> imageUrlList) {
        String createReportImageQuery = "insert into ReportImages(reportIdx) values (?);";
        int createReportImageParams = reportIdx;
        this.jdbcTemplate.update(createReportImageQuery, createReportImageParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int reportImageIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String createImageQuery = "insert into Images(reportImageIdx, imageUrl) values(?, ?);";
        Object[] createImageParams;
        for (String imageUrl : imageUrlList) {
            createImageParams = new Object[]{reportImageIdx, imageUrl};
            this.jdbcTemplate.update(createImageQuery, createImageParams);
        }
    }
}
