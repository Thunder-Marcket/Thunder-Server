package com.example.demo.src.Brands;

import com.example.demo.src.Brands.model.GetBrandListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BrandDao {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetBrandListRes> getBrands(int userIdx) {
        String getBrandListQuery = "select\n" +
                "    B.brandName,\n" +
                "    B.brandSubName,\n" +
                "    concat((select count(I.itemIdx) from Items I inner join Tags T on I.itemIdx = T.itemIdx\n" +
                "            where T.tagName = B.brandName), '개') AS brandItemCount,\n" +
                "    exists(select BF.brand_followIdx from Brand_Follows BF where B.brandIdx = BF.followingBrandIdx AND BF.followerUserIdx = ?) AS isFollowCheck,\n" +
                "    case\n" +
                "        when B.imageUrl is null then '이미지 없음'\n" +
                "        ELSE B.imageUrl\n" +
                "    END AS storeImageUrl\n" +
                "from Brands B";
        int getBrandListParams = userIdx;

        return this.jdbcTemplate.query(getBrandListQuery,
                (rs, rowNum) -> new GetBrandListRes(
                        rs.getString("brandName"),
                        rs.getString("brandSubName"),
                        rs.getString("brandItemCount"),
                        rs.getInt("isFollowCheck"),
                        rs.getString("storeImageUrl")
                ),
                getBrandListParams);
    }
}
