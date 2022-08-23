package com.example.demo.src.Items;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Items.model.GetItemInfoRes;
import com.example.demo.src.Items.model.GetItemListRes;
import com.example.demo.utils.JwtService;
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
    private final JwtService jwtService;
    @Autowired
    private final ItemService itemService;

    public ItemController(ItemProvider itemProvider, JwtService jwtService, ItemService itemService) {
        this.itemProvider = itemProvider;
        this.jwtService = jwtService;
        this.itemService = itemService;
    }


    /**
     *  추천 상품 조회(최신순) API
     *  [GET] /items
     *  검색어로 상품 가져오는 API
     *  [GET] /items?search=
     * @return BaseResponse<List<GetItemListRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetItemListRes>> getItems(@RequestParam(required = false) String search){
        try{
            int userIdx = jwtService.getUserIdx();


            if (search == null){
                List<GetItemListRes> getItemListRes = itemProvider.getNewItems(userIdx);
                return new BaseResponse<>(getItemListRes);
            }

            List<GetItemListRes> getItemListRes = itemProvider.getSearchItems(userIdx, search);
            return new BaseResponse<>(getItemListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상품 상세 정보 가져오는 API
     * [GET] /items/:itemIdx
     * @return BaseResponse<GetItemInfoRes>
     */
    // @ResponseBody
    // @GetMapping("")
    // public BaseResponse<GetItemInfoRes> getItemInfo(@PathVariable("itemIdx") int itemIdx){
        
    // }





}
