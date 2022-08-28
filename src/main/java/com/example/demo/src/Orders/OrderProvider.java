package com.example.demo.src.Orders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Orders.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;

    @Autowired
    public OrderProvider(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public GetDirectOrderRes getDirectOrderPage(int userIdx, int itemIdx, int usePoint) throws BaseException {
        try{
            GetDirectOrderRes getDirectOrderRes = orderDao.getDirectOrderRes(userIdx, itemIdx, usePoint);
            return getDirectOrderRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetIndirectOrderRes getIndirectOrderPage(int userIdx, int itemIdx, int usePoint) throws BaseException {
        try{

            GetIndirectOrderRes getIndirectOrderRes = orderDao.getIndirectOrderRes(userIdx, itemIdx, usePoint);
            return getIndirectOrderRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkItem(PostOrderReq postOrderReq) throws BaseException {
        try{
            return orderDao.checkItem(postOrderReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPurchaseOrderRes> getPurchase(int userIdx) throws BaseException {
        try{
            List<GetPurchaseOrderRes> getPurchaseOrderRes = orderDao.getPurchase(userIdx);
            return getPurchaseOrderRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSaleOrderRes> getSale(int userIdx) throws BaseException {
        try{
            List<GetSaleOrderRes> getSaleOrderRes = orderDao.getSale(userIdx);
            return getSaleOrderRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
