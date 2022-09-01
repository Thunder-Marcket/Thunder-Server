package com.example.demo.src.Naver;

import com.example.demo.config.BaseException;
import com.example.demo.src.Naver.model.GetLocationRes;
import com.example.demo.src.Naver.model.PostTransReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class NaverDao {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addPoint(PostTransReq postTransReq, GetLocationRes getLocationRes) {
        String addPointQuery = "UPDATE Addresss\n" +
                "set Addresss.latitude = ?, Addresss.longitude = ?\n" +
                "where Addresss.addressIdx = ?;";
        Object[] addPointParams = new Object[]{
                getLocationRes.getX(),
                getLocationRes.getY(),
                postTransReq.getAddressIdx()
        };

        this.jdbcTemplate.update(addPointQuery, addPointParams);
    }

    public int checkAddress(int userIdx, PostTransReq postTransReq) throws BaseException {
        String checkAddressQuery = "select exists(select A.addressIdx from Addresss A where A.userIdx = ? and A.addressIdx = ?);";
        Object[] checkAddressParam = new Object[]{userIdx, postTransReq.getAddressIdx()};

        return this.jdbcTemplate.queryForObject(checkAddressQuery, int.class, checkAddressParam);
    }
}
