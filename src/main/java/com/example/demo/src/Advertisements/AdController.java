package com.example.demo.src.Advertisements;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Advertisements.model.GetMainAdRes;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/advertisements")
public class AdController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AdProvider adProvider;
    private final AdService adService;

    @Autowired
    public AdController(AdProvider adProvider, AdService adService) {
        this.adProvider = adProvider;
        this.adService = adService;
    }

    /**
     * 메인 페이지에 보여질 광고 조회 API
     * [GET] /advertisements/main
     */
    @ResponseBody
    @GetMapping("/main")
    public BaseResponse<List<GetMainAdRes>> getMainAd(){
        try{
            List<GetMainAdRes> getMainAdRes = adProvider.getMainAd();
            return new BaseResponse<>(getMainAdRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
