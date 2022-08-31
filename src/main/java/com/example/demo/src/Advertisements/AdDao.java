package com.example.demo.src.Advertisements;

import com.example.demo.src.Advertisements.model.GetMainAdRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AdDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetMainAdRes> getMainAd() {
        String getMainAdQuery = "select\n" +
                "    Ad.adUrl,\n" +
                "    Ad.content,\n" +
                "    Ad.advertisementIdx,\n" +
                "    (select imageUrl from Images where Ad.advertisementIdx = Images.advertisementIdx) AS mainAdImage\n" +
                "from Advertisements Ad;";

        return this.jdbcTemplate.query(getMainAdQuery,
                (rs, rowNum) -> new GetMainAdRes(
                        rs.getString("adUrl"),
                        rs.getString("content"),
                        rs.getString("mainAdImage"),
                        rs.getInt("advertisementIdx"))
        );
    }
}
