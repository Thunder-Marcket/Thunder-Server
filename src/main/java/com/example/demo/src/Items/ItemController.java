package com.example.demo.src.Items;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Items.model.GetItemListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ItemProvider itemProvider;

    @Autowired
    private final ItemService itemService;

    public ItemController(ItemProvider itemProvider, ItemService itemService) {
        this.itemProvider = itemProvider;
        this.itemService = itemService;
    }


    /**
     *  추천 상품 조회(최신순) API
     *  [GET] /items
     * @return BaseResponse<List<GetItemListRes>>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetItemListRes>> getItems(@PathVariable("userIdx") int userIdx){
        try{

            List<GetItemListRes> getItemListRes = itemProvider.getNewItems(userIdx);
            return new BaseResponse<>(getItemListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 검색어로 상품 가져오는 API
     * [GET] /items?search=
     * @return BaseResponse<List<GetItemListRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetItemListRes>> getSearchItems(@PathVariable("userIdx") int userIdx, @RequestParam("search") String search){
        try{
            List<GetItemListRes> getItemListRes = itemProvider.getSearchItems(userIdx, search);
            return new BaseResponse<>(getItemListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
