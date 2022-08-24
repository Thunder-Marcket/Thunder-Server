package com.example.demo.src.Brands;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Brands.model.GetBrandListRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BrandProvider brandProvider;
    private final BrandService brandService;
    private final JwtService jwtService;

    @Autowired
    public BrandController(BrandProvider brandProvider, BrandService brandService, JwtService jwtService) {
        this.brandProvider = brandProvider;
        this.brandService = brandService;
        this.jwtService = jwtService;
    }


    /**
     *  모든 브랜드 조회 API
     *  [GET] /brands
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetBrandListRes>> getBrands(){
        try{
            int userIdx = jwtService.getUserIdx();

            List<GetBrandListRes> getBrandListRes = brandProvider.getBrands(userIdx);
            return new BaseResponse<>(getBrandListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
