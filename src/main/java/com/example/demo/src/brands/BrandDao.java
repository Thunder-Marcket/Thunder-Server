package com.example.demo.src.brands;

import com.example.demo.src.brands.model.GetBrandListRes;
import com.example.demo.src.brands.model.GetFollowBrandRes;
import com.example.demo.src.Items.model.GetItemListRes;
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
        String getBrandListQuery =
                "select\n" +
                "    B.brandIdx,\n" +
                "    B.brandName,\n" +
                "    B.brandSubName,\n" +
                "    concat((select count(I.itemIdx) from Items I inner join Tags T on I.itemIdx = T.itemIdx\n" +
                "            where T.tagName = B.brandName), '개') AS brandItemCount,\n" +
                "    exists(select BF.brand_followIdx from Brand_Follows BF where B.brandIdx = BF.followingBrandIdx AND BF.followerUserIdx = ?) AS isFollowCheck,\n" +
                "    case\n" +
                "        when B.imageUrl is null then '이미지 없음'\n" +
                "        ELSE B.imageUrl\n" +
                "    END AS storeImageUrl\n" +
                "from Brands B;";
        int getBrandListParams = userIdx;

        return this.jdbcTemplate.query(getBrandListQuery,
                (rs, rowNum) -> new GetBrandListRes(
                        rs.getInt("brandIdx"),
                        rs.getString("brandName"),
                        rs.getString("brandSubName"),
                        rs.getString("brandItemCount"),
                        rs.getInt("isFollowCheck"),
                        rs.getString("storeImageUrl")
                ),
                getBrandListParams);
    }

    public List<GetBrandListRes> getSearchBrands(int userIdx, String search) {
        String getSearchBrandQuery = "select\n" +
                "    B.brandIdx,\n" +
                "    B.brandName,\n" +
                "    B.brandSubName,\n" +
                "    concat((select count(I.itemIdx) from Items I inner join Tags T on I.itemIdx = T.itemIdx\n" +
                "            where T.tagName = B.brandName), '개') AS brandItemCount,\n" +
                "    exists(select BF.brand_followIdx from Brand_Follows BF where B.brandIdx = BF.followingBrandIdx AND BF.followerUserIdx = ?) AS isFollowCheck,\n" +
                "    case\n" +
                "        when B.imageUrl is null then '이미지 없음'\n" +
                "        ELSE B.imageUrl\n" +
                "    END AS storeImageUrl\n" +
                "from Brands B\n" +
                "where INSTR(B.brandName, ?);";
        Object[] getSearchBrandParams = new Object[]{userIdx, search};

        return this.jdbcTemplate.query(getSearchBrandQuery,
                (rs, rowNum) -> new GetBrandListRes(
                        rs.getInt("brandIdx"),
                        rs.getString("brandName"),
                        rs.getString("brandSubName"),
                        rs.getString("brandItemCount"),
                        rs.getInt("isFollowCheck"),
                        rs.getString("storeImageUrl")
                ), getSearchBrandParams);
    }

    public List<GetFollowBrandRes> getFollowBrandList(int userIdx) {
        String getFollowBrandQuery = "select\n" +
                "    B.brandIdx,\n" +
                "    B.brandName,\n" +
                "    B.brandSubName,\n" +
                "    concat((select count(I.itemIdx) from Items I inner join Tags T on I.itemIdx = T.itemIdx\n" +
                "            where T.tagName = B.brandName), '개') AS brandItemCount,\n" +
                "    exists(select BF.brand_followIdx from Brand_Follows BF where B.brandIdx = BF.followingBrandIdx AND BF.followerUserIdx = ?) AS isFollowCheck,\n" +
                "    case\n" +
                "        when B.imageUrl is null then '이미지 없음'\n" +
                "        ELSE B.imageUrl\n" +
                "    END AS storeImageUrl,\n" +
                "    B.brandName\n" +
                "from Brands B\n" +
                "inner join Brand_Follows F on B.brandIdx = F.followingBrandIdx\n" +
                "where F.followerUserIdx = ?\n" +
                "and F.status = 'enable';";

        Object[] getFollowBrandParams = new Object[]{userIdx, userIdx};

        return this.jdbcTemplate.query(getFollowBrandQuery,

                (rs, rowNum) -> new GetFollowBrandRes(
                        new GetBrandListRes(
                                rs.getInt("brandIdx"),
                                rs.getString("brandName"),
                                rs.getString("brandSubName"),
                                rs.getString("brandItemCount"),
                                rs.getInt("isFollowCheck"),
                                rs.getString("storeImageUrl")
                        ),

                        getFollowBrandItem(userIdx, rs.getString("brandName"))
                ),
                getFollowBrandParams);
    }


    public List<GetItemListRes> getFollowBrandItem(int userIdx, String brandName) {
        String getFollowBrandItemQuery =
                "select I.itemIdx,\n" +
                "       concat(I.cost, '원') AS cost,\n" +
                "       I.itemName,\n" +
                "       case\n" +
                "           when I.address is null then '지역정보 없음'\n" +
                "           ELSE I.address\n" +
                "       END AS address,\n" +
                "       case\n" +
                "           when datediff(now(), I.createdAt) < 1 then concat(abs(hour(now()) - hour(I.createdAt)), '시간 전')\n" +
                "           ELSE concat(datediff(now(), I.createdAt), '일 전')\n" +
                "        END AS period,\n" +
                "       (select Images.imageUrl from Images\n" +
                "                 where Images.imageIdx = (select min(Images.imageIdx) from Images\n" +
                "                                                                      inner join ItemImages II on Images.itemImageIdx = II.itemImageIdx\n" +
                "                                                                      inner join Items I2 on II.itemIdx = I2.itemIdx\n" +
                "                                                                      where I2.itemIdx = I.itemIdx)) AS imageUrl,\n" +
                "       I.isSafePayment,\n" +
                "       case\n" +
                "           when (select 1 from Likes inner join Users U on Likes.userIdx = U.userIdx\n" +
                "               where U.userIdx = ? and Likes.itemIdx = I.itemIdx)\n" +
                "               then 1\n" +
                "            ELSE 0\n" +
                "        END isLike,\n" +
                "\n" +
                "       (select count(likeIdx) from Likes where I.itemIdx = Likes.itemIdx) AS likeCnt,\n" +
                "       I.isCanCheck,\n" +
                "       I.isAdItem\n" +
                "\n" +
                "from Items I\n" +
                "inner join Tags T on I.itemIdx = T.itemIdx\n" +
                "where T.tagName = ?\n" +
                "\n" +
                "order by I.createdAt DESC;\n";
        Object[] getFollowBrandItemParams = new Object[]{userIdx, brandName};

        return this.jdbcTemplate.query(getFollowBrandItemQuery,
                (rs, rowNum) -> new GetItemListRes(
                        rs.getInt("itemIdx"),
                        rs.getString("cost"),
                        rs.getString("itemName"),
                        rs.getString("address"),
                        rs.getString("period"),
                        rs.getString("imageUrl"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("isLike"),
                        rs.getInt("likeCnt"),
                        rs.getInt("isCanCheck"),
                        rs.getInt("isAdItem")
                        //rs.getString("status")
                ), getFollowBrandItemParams);
    }

    public GetFollowBrandRes getBrandItem(int userIdx, int brandIdx) {
        String getBrandItemQuery = "select\n" +
                "    B.brandIdx,\n" +
                "    B.brandName,\n" +
                "    B.brandSubName,\n" +
                "    concat((select count(I.itemIdx) from Items I inner join Tags T on I.itemIdx = T.itemIdx\n" +
                "            where T.tagName = B.brandName), '개') AS brandItemCount,\n" +
                "    exists(select BF.brand_followIdx from Brand_Follows BF where B.brandIdx = BF.followingBrandIdx AND BF.followerUserIdx = ?) AS isFollowCheck,\n" +
                "    case\n" +
                "        when B.imageUrl is null then '이미지 없음'\n" +
                "        ELSE B.imageUrl\n" +
                "    END AS storeImageUrl,\n" +
                "    B.brandName\n" +
                "from Brands B\n" +
                "where B.brandIdx = ?;";
        Object[] getBrandItemParams = new Object[]{userIdx, brandIdx};

        return this.jdbcTemplate.queryForObject(getBrandItemQuery,
                (rs, rowNum) -> new GetFollowBrandRes(
                        new GetBrandListRes(
                                rs.getInt("brandIdx"),
                                rs.getString("brandName"),
                                rs.getString("brandSubName"),
                                rs.getString("brandItemCount"),
                                rs.getInt("isFollowCheck"),
                                rs.getString("storeImageUrl")
                        ),

                        getFollowBrandItem(userIdx, rs.getString("brandName"))
                ), getBrandItemParams);
    }
}
