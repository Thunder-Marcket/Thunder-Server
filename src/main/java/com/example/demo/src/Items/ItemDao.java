package com.example.demo.src.Items;

import com.example.demo.config.BaseException;
import com.example.demo.src.Items.model.GetItemListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ItemDao {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetItemListRes> getNewItems(int userIdx) {

        String getItemQuery = "select I.itemIdx,\n" +
                "       I.cost,\n" +
                "       I.itemName,\n" +
                "       I.address,\n" +
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
                "\n" +
                "order by I.createdAt DESC;";
        int getItemParams = userIdx;


        return this.jdbcTemplate.query(getItemQuery,
                (rs, rowNum) -> new GetItemListRes(
                        rs.getInt("itemIdx"),
                        rs.getInt("cost"),
                        rs.getString("itemName"),
                        rs.getString("address"),
                        rs.getString("period"),
                        rs.getString("imageUrl"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("isLike"),
                        rs.getInt("likeCnt"),
                        rs.getInt("isCanCheck"),
                        rs.getInt("isAdItem")),
                getItemParams);
    }



    public List<GetItemListRes> getSearchItems(int userIdx, String search) throws BaseException {
        String insertSearchQuery = "insert into Searchs (searchText, userIdx) VALUES (?,?);";
        Object[] insertSearchParams = new Object[]{search, userIdx};

        this.jdbcTemplate.update(insertSearchQuery, insertSearchParams);




        String getSearchItemQuery = "select I.itemIdx,\n" +
                "       I.cost,\n" +
                "       I.itemName,\n" +
                "       I.address,\n" +
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
                "\n" +
                "where instr(I.itemName, ?) or (select instr(T.tagName, ?) from Tags T where T.itemIdx = I.itemIdx)\n" +
                "\n" +
                "order by I.createdAt DESC;";
        Object[] getSearchItemParam = new Object[]{userIdx, search, search};

        return this.jdbcTemplate.query(getSearchItemQuery,
                (rs, rowNum) -> new GetItemListRes(
                        rs.getInt("itemIdx"),
                        rs.getInt("cost"),
                        rs.getString("itemName"),
                        rs.getString("address"),
                        rs.getString("period"),
                        rs.getString("imageUrl"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("isLike"),
                        rs.getInt("likeCnt"),
                        rs.getInt("isCanCheck"),
                        rs.getInt("isAdItem")),
                getSearchItemParam);

    }


}
