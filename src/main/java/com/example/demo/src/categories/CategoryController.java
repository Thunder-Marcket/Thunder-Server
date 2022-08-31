package com.example.demo.src.categories;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.categories.model.GetCategoryItemsRes;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.POST_CATEGORY_EMPTY_ID;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final CategoryProvider categoryProvider;

    /**
     * 대분류 카테고리에 해당하는 상품 리스트 조회 API
     * [GET] /categories?categoryIdx=
     *
     * 중분류 카테고리에 해당하는 상품 리스트 조회 API
     * [GET] /categories?categoryIdx= &subCategoryIdx=
     *
     * 소분류 카테고리에 해당하는 상품 리스트 조회 API
     * [GET] /categories?categoryIdx= &subCategoryIdx= &subSubcategoryIdx=
     *
     * @return BaseResponse<GetCategoryItemsRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetCategoryItemsRes> getItemsByCategory(@RequestParam(defaultValue = "0") int categoryIdx,
                                                                @RequestParam(required = false, defaultValue = "0") int subCategoryIdx,
                                                                @RequestParam(required = false, defaultValue = "0") int subSubcategoryIdx) {
        try {
            logger.debug("categoryIdx = {}", categoryIdx);
            logger.debug("subCategoryIdx = {}", subCategoryIdx);
            logger.debug("subSubcategoryIdx = {}", subSubcategoryIdx);
            int userIdx = jwtService.getUserIdx();
            if (categoryIdx == 0) {
                throw new BaseException(POST_CATEGORY_EMPTY_ID);
            }
            GetCategoryItemsRes getCategoryItemsRes = categoryProvider.getItemsByCategory(userIdx, categoryIdx, subCategoryIdx, subSubcategoryIdx);
            return new BaseResponse<>(getCategoryItemsRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
