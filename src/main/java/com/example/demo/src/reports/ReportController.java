package com.example.demo.src.reports;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.reports.model.PostReportReq;
import com.example.demo.src.reports.model.PostReportRes;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@AllArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReportService reportService;
    @Autowired
    private final JwtService jwtService;

    /**
     * 신고하기(1:1 문의하기) API
     * [POST] /reports/:userIdx
     * @return BaseResponse<PostReportRes>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostReportRes> createReport(@PathVariable("userIdx") int userIdx,
                                                    @RequestBody PostReportReq postReportReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostReportRes postReportRes = reportService.createReport(userIdx,postReportReq);
            return new BaseResponse<>(postReportRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
