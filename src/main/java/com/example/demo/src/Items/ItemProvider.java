
package com.example.demo.src.Items;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Items.model.GetItemInfoRes;
import com.example.demo.src.Items.model.GetItemListRes;
import com.example.demo.src.Items.model.GetRegistItem;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ItemProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ItemDao itemDao;
    private final JwtService jwtService;

    @Autowired
    public ItemProvider(ItemDao itemDao, JwtService jwtService) {
        this.itemDao = itemDao;
        this.jwtService = jwtService;
    }


    public List<GetItemListRes> getNewItems(int userIdx) throws BaseException {
        try{

            List<GetItemListRes> getItemListRes = itemDao.getNewItems(userIdx);
            return getItemListRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetItemListRes> getSearchItems(int userIdx, String search) throws BaseException {
        try{
            List<GetItemListRes> getItemListRes = itemDao.getSearchItems(userIdx, search);
            return getItemListRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }


    public GetItemInfoRes getItemInfo(int buyUserIdx, int itemIdx) throws BaseException {
        try{
            GetItemInfoRes getItemInfoRes = itemDao.getItemInfo(buyUserIdx, itemIdx);
            return getItemInfoRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }


    }

    public List<GetRegistItem> getRegistItem(int userIdx) throws BaseException {
        try{
            List<GetRegistItem> getItemListRes = itemDao.getRegistItem(userIdx);
            return getItemListRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

