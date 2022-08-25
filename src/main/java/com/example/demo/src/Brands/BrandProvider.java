package com.example.demo.src.Brands;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Brands.model.GetBrandListRes;
import com.example.demo.src.Brands.model.GetFollowBrandRes;
import com.example.demo.src.Items.model.GetItemListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BrandProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BrandDao brandDao;

    @Autowired
    public BrandProvider(BrandDao brandDao) {
        this.brandDao = brandDao;
    }

    public List<GetBrandListRes> getBrands(int userIdx) throws BaseException {
        try{
            List<GetBrandListRes> getBrandListRes = brandDao.getBrands(userIdx);
            return getBrandListRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBrandListRes> getSearchBrands(int userIdx, String search) throws BaseException {
        try{
            List<GetBrandListRes> getBrandListRes = brandDao.getSearchBrands(userIdx, search);
            return getBrandListRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowBrandRes> getFollowBrandList(int userIdx) throws BaseException {
        try{
            List<GetFollowBrandRes> getFollowBrandRes = brandDao.getFollowBrandList(userIdx);
            return getFollowBrandRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }


    }


    public GetFollowBrandRes getBrandItems(int userIdx, int brandIdx) throws BaseException {
        try{
            GetFollowBrandRes getFollowBrandRes = brandDao.getBrandItem(userIdx, brandIdx);
            return getFollowBrandRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
