
package com.example.demo.src.Items;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Items.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
     @ResponseBody
     @GetMapping("/{itemIdx}")
     public BaseResponse<GetItemInfoRes> getItemInfo(@PathVariable("itemIdx") int itemIdx){
         try{
             int buyUserIdx = jwtService.getUserIdx();

             GetItemInfoRes getItemInfoRes = itemProvider.getItemInfo(buyUserIdx, itemIdx);
             return new BaseResponse<>(getItemInfoRes);
         } catch (BaseException exception){
             return new BaseResponse<>(exception.getStatus());
         }
     }


    /**
     *  상품 등록 API
     *  [POST] /items
     * @return BaseResponse<PostItemRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostItemRes> createItem(@RequestBody PostItemReq postItemReq){
        try{
            if(postItemReq.getUserIdx() != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(postItemReq.getImageUrlList().size() == 0){
                return new BaseResponse<>(POST_ITEMS_NEED_IMAGES);
            }

            if(postItemReq.getImageUrlList().size() > 12){
                return new BaseResponse<>(POST_ITEMS_OVER_IMAGES);
            }

            if(postItemReq.getItemName() == null){
                return new BaseResponse<>(POST_ITEMS_NEED_ITEM_NAME);
            }

            if(postItemReq.getItemName().length() > 45){
                return new BaseResponse<>(POST_ITEMS_INVAIlD_ITEM_NAME);
            }

            if(postItemReq.getItemContent() == null || postItemReq.getItemContent().length() < 10){
                return new BaseResponse<>(POST_ITEMS_UNDER_ITEM_CONTENT);
            }


            PostItemRes postItemRes = itemService.createItem(postItemReq);
            return new BaseResponse<>(postItemRes);
        } catch (BaseException exception){
         return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 등록 상품 조회 API
     * [GET] /items/regist/:userIdx
     * @return BaseResponse<List<GetItemListRes>>
     */
    @ResponseBody
    @GetMapping("/regist/{userIdx}")
    public BaseResponse<List<GetRegistItem>> getRegistItem(@PathVariable("userIdx") int userIdx){
        try{
            if(userIdx != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetRegistItem> getItemListRes = itemProvider.getRegistItem(userIdx);
            return new BaseResponse<>(getItemListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 등록 상품 수정 API
     * [PATCH] /items/:itemIdx
     * @return BaseResponse<PatchItemRes>
     */
    @ResponseBody
    @PatchMapping("/{itemIdx}")
    public BaseResponse<PatchItemRes> modifyItem(@PathVariable("itemIdx") int itemIdx,
                                                 @RequestBody PatchItemReq patchItemReq){
        try{
            if(patchItemReq.getUserIdx() != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(itemProvider.getExistItem(itemIdx, patchItemReq.getUserIdx()) == 0){
                return new BaseResponse<>(PATCH_ITEMS_NULL_ITEM);
            }
//            if(patchItemReq.getImageUrlList().size() == 0){
//                return new BaseResponse<>(POST_ITEMS_NEED_IMAGES);
//            }
//
//            if(patchItemReq.getImageUrlList().size() > 12){
//                return new BaseResponse<>(POST_ITEMS_OVER_IMAGES);
//            }

            if(patchItemReq.getItemName() == null){
                return new BaseResponse<>(POST_ITEMS_NEED_ITEM_NAME);
            }

            if(patchItemReq.getItemName().length() > 45){
                return new BaseResponse<>(POST_ITEMS_INVAIlD_ITEM_NAME);
            }

            if(patchItemReq.getItemContent() == null || patchItemReq.getItemContent().length() < 10){
                return new BaseResponse<>(POST_ITEMS_UNDER_ITEM_CONTENT);
            }

            PatchItemRes patchItemRes = itemService.modifyItem(patchItemReq, itemIdx);
            return new BaseResponse<>(patchItemRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
