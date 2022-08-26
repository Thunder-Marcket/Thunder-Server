package com.example.demo.src.salesViews;

import com.example.demo.config.BaseException;
import com.example.demo.src.salesViews.model.PatchSalesViewsReq;
import com.example.demo.src.salesViews.model.PatchSalesViewsRes;
import com.example.demo.src.salesViews.model.SalesViews;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_SALESVIEWS_STATUS;

@AllArgsConstructor
@Service
public class SalesViewsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SalesViewsDao salesViewsDao;

    public PatchSalesViewsRes modifySalesViewsStatus(PatchSalesViewsReq patchSalesViewsReq) throws BaseException {
        try {
            List<SalesViews> viewItemIdxList = new ArrayList<>();

            for (SalesViews salesViews : patchSalesViewsReq.getViewItemIdxList()) {
                logger.debug("salesViews: = {}", salesViews);
                int result = salesViewsDao.modifySalesViewsStatus(salesViews);
                if (result == 0) {
                    throw new BaseException(MODIFY_FAIL_SALESVIEWS_STATUS);
                }
                viewItemIdxList.add(salesViews);
            }
            return new PatchSalesViewsRes(viewItemIdxList);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}