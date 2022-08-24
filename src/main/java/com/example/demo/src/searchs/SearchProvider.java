package com.example.demo.src.searchs;

import com.example.demo.config.BaseException;
import com.example.demo.src.searchs.model.GetBrands;
import com.example.demo.src.searchs.model.GetSearch;
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

    public List<GetSearch> getSearches(int userIdx) throws BaseException {
        try {
            List<GetSearch> getSearches = searchDao.getSearches(userIdx);
            return getSearches;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBrands> getBrands(int userIdx) throws BaseException {
        try {
            List<GetBrands> getBrands = searchDao.getBrands(userIdx);
            return getBrands;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
