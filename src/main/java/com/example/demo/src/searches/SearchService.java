package com.example.demo.src.searches;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.searches.model.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@AllArgsConstructor
@Service
public class SearchService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchProvider searchProvider;
    private final SearchDao searchDao;

    public GetSearchesRes modifySearchStatus(int userIdx, PatchSearchesReq patchSearchesReq) throws BaseException {
        int result = searchDao.modifySearchStatus(userIdx, patchSearchesReq);
        if (result == 0) {
            throw new BaseException(MODIFY_FAIL_SEARCH_STATUS);
        }
        try {
            List<GetSearch> getSearches = searchProvider.getSearches(userIdx);
            List<GetBrands> getBrands = searchProvider.getBrands(userIdx);
            GetSearchesRes getSearchesRes = new GetSearchesRes(getSearches, getBrands);
            return getSearchesRes;
        } catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetSearchesRes modifyAllSearchStatus(int userIdx) throws BaseException {
        int result = searchDao.modifyAllSearchStatus(userIdx);
        if (result == 0) {
            throw new BaseException(MODIFY_FAIL_SEARCH_STATUS);
        }
        try {
            List<GetSearch> getSearches = searchProvider.getSearches(userIdx);
            List<GetBrands> getBrands = searchProvider.getBrands(userIdx);
            GetSearchesRes getSearchesRes = new GetSearchesRes(getSearches, getBrands);
            return getSearchesRes;
        }catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
