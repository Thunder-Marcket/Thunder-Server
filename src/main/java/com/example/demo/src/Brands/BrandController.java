package com.example.demo.src.Brands;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Brands.model.GetBrandListRes;
import com.example.demo.src.Brands.model.GetFollowBrandRes;
import com.example.demo.src.Items.model.GetItemListRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

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
     *  검색어로 브랜드 조회 API
     *  [GET] /brands?search
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetBrandListRes>> getBrands(@RequestParam(required = false) String search){
        try{
            int userIdx = jwtService.getUserIdx();

            if(search != null){
                List<GetBrandListRes> getBrandListRes = brandProvider.getSearchBrands(userIdx, search);
                return new BaseResponse<>(getBrandListRes);
            }

            List<GetBrandListRes> getBrandListRes = brandProvider.getBrands(userIdx);
            return new BaseResponse<>(getBrandListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 내가 팔로우 한 브랜드 조회 API
     * [GET] /brands/:userIdx
     * @return BaseRespons<List<GetBrandListRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetFollowBrandRes>> getFollowBrandByUser(@PathVariable("userIdx") int userIdx){
        try{
            if(userIdx != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }


            List<GetFollowBrandRes> getFollowBrandListRes = brandProvider.getFollowBrandList(userIdx);
            return new BaseResponse<>(getFollowBrandListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 해당 브랜드의 상품 조회 API
     * [GET] /brands/:brandIdx/items
     * @return BaseResponse<GetFollowBrandRes>
     */
    @ResponseBody
    @GetMapping("/{brandIdx}/items")
    public BaseResponse<GetFollowBrandRes> getBrandItems(@PathVariable("brandIdx") int brandIdx){
        try{
            int userIdx = jwtService.getUserIdx();

            GetFollowBrandRes getFollowBrandRes = brandProvider.getBrandItems(userIdx, brandIdx);
            return new BaseResponse<>(getFollowBrandRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
