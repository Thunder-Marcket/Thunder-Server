package com.example.demo.src.salesViews;

import com.example.demo.config.BaseException;
import com.example.demo.src.salesViews.model.GetSalesViewsRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.DELETED_SALESVIEWS;

@AllArgsConstructor
@Service
public class SalesViewsProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SalesViewsDao salesViewsDao;


    public List<GetSalesViewsRes> getSalesViews(int userIdx) throws BaseException {

        try {
            List<GetSalesViewsRes> getSalesViewsResList = salesViewsDao.getSalesViews(userIdx);
            return getSalesViewsResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
