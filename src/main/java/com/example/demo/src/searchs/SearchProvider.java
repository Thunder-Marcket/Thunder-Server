package com.example.demo.src.searchs;

import com.example.demo.config.BaseException;
import com.example.demo.src.searchs.model.GetSearchesRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@AllArgsConstructor
@Service
public class SearchProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;

    public List<GetSearchesRes> getSearches(int userIdx) throws BaseException {
        try {
            List<GetSearchesRes> getSearchesResList = searchDao.getSearches(userIdx);
            return getSearchesResList;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
