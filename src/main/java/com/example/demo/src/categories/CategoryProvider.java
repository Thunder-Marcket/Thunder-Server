package com.example.demo.src.categories;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.categories.model.CategoryInfo;
import com.example.demo.src.categories.model.GetCategoryItemsRes;
import com.example.demo.src.categories.model.ItemInfo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@AllArgsConstructor
@Service
public class CategoryProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CategoryDao categoryDao;

    public GetCategoryItemsRes getItemsByCategory(int userIdx, int categoryIdx, int subCategoryIdx, int subSubcategoryIdx) throws BaseException {
        CategoryInfo categoryInfo = null;
        List<CategoryInfo> categoryInfoList = null;
        List<ItemInfo> itemInfoList = null;

        logger.debug("CategoryProvider");

        if (categoryIdx != 0 && subCategoryIdx == 0 && subSubcategoryIdx == 0) {
            categoryInfo = categoryDao.getCategoryInfo(categoryIdx);
            categoryInfoList = categoryDao.getCategoryInfoList(categoryIdx);
            itemInfoList = categoryDao.getItemsByCategory(userIdx, categoryIdx);

            return new GetCategoryItemsRes(categoryInfo, categoryInfoList, itemInfoList);
        } else if (categoryIdx != 0 && subCategoryIdx != 0 && subSubcategoryIdx == 0) {

            if(categoryDao.checkSubCategory(categoryIdx) == 1) {
                categoryInfo = categoryDao.getCategoryInfoBySub(subCategoryIdx);
                categoryInfoList = categoryDao.getCategoryInfoListBySub(subCategoryIdx);
                itemInfoList = categoryDao.getItemsByTwoCategory(userIdx, categoryIdx, subCategoryIdx);
            }

            return new GetCategoryItemsRes(categoryInfo, categoryInfoList, itemInfoList);

        } else if (categoryIdx != 0 && subCategoryIdx != 0 && subSubcategoryIdx != 0) {

            if(categoryDao.checkSubSubCategory(subCategoryIdx) == 1) {
                categoryInfo = categoryDao.getCategoryInfoBySubSub(subSubcategoryIdx);
                itemInfoList = categoryDao.getItemsByThreeCategory(userIdx, categoryIdx, subCategoryIdx, subSubcategoryIdx);
            }
            return new GetCategoryItemsRes(categoryInfo, categoryInfoList, itemInfoList);

        } else {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
