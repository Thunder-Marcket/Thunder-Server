package com.example.demo.src.salesViews;

import com.example.demo.config.BaseException;
import com.example.demo.src.salesViews.model.PatchSalesViewsRes;
import com.example.demo.src.salesViews.model.SalesViews;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_SALESVIEWS_STATUS;

@AllArgsConstructor
@Service
public class SalesViewsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SalesViewsDao salesViewsDao;

    public void modifySalesViewsStatus(PatchSalesViewsRes patchSalesViewsRes) throws BaseException {
        try {
            for (SalesViews salesViews : patchSalesViewsRes.getViewItemIdxList()) {
                logger.debug("salesViews: = {}", salesViews);
                int result = salesViewsDao.modifySalesViewsStatus(salesViews);
                if (result == 0) {
                    throw new BaseException(MODIFY_FAIL_SALESVIEWS_STATUS);
                }
            }
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
