package com.example.demo.src.Orders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Orders.model.PostOrderReq;
import com.example.demo.src.Orders.model.PostOrderRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderProvider orderProvider;
    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderProvider orderProvider, OrderDao orderDao) {
        this.orderProvider = orderProvider;
        this.orderDao = orderDao;
    }

    public PostOrderRes createOrder(PostOrderReq postOrderReq) throws BaseException {
        if(orderProvider.checkItem(postOrderReq) == 0){
            throw new BaseException(POST_ORDERS_INVALID_ITEM);
        }

        try{
            PostOrderRes postOrderRes = orderDao.createOrder(postOrderReq);
            return postOrderRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
