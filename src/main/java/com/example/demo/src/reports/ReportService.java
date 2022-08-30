package com.example.demo.src.reports;

import com.example.demo.config.BaseException;
import com.example.demo.src.reports.model.PostReportReq;
import com.example.demo.src.reports.model.PostReportRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@AllArgsConstructor
@Service
public class ReportService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReportDao reportDao;

    public PostReportRes createReport(int userIdx, PostReportReq postReportReq) throws BaseException {
        try {
            return new PostReportRes(reportDao.createReport(userIdx, postReportReq));
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
