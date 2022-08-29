
package com.example.demo.src.Items;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Items.model.PatchItemReq;
import com.example.demo.src.Items.model.PatchItemRes;
import com.example.demo.src.Items.model.PostItemReq;
import com.example.demo.src.Items.model.PostItemRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ItemService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ItemProvider itemProvider;
    private final ItemDao itemDao;

    @Autowired
    public ItemService(ItemProvider itemProvider, ItemDao itemDao) {
        this.itemProvider = itemProvider;
        this.itemDao = itemDao;
    }

    public PostItemRes createItem(PostItemReq postItemReq) throws BaseException {
        try{
            int result = itemDao.createItem(postItemReq);
            return new PostItemRes(result);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchItemRes modifyItem(PatchItemReq patchItemReq, int itemIdx) throws BaseException {
        try{
            PatchItemRes patchItemRes = itemDao.modifyItem(patchItemReq, itemIdx);
            return patchItemRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

