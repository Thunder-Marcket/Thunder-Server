
package com.example.demo.src.Items;

import com.example.demo.config.BaseException;
import com.example.demo.src.Items.model.Comments;
import com.example.demo.src.Items.model.GetItemInfoRes;
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

    // 추천 상품 가져오는 쿼리...
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


    // 검색어로 상품 가져오기
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


    // 상세 상품 정보 가져오기
    public GetItemInfoRes getItemInfo(int buyUserIdx, int itemIdx) {
        String getItemInfoQuery = "select I.cost,\n" +
                "       I.itemName,\n" +
                "       I.address,\n" +
                "       case\n" +
                "           when datediff(now(), I.createdAt) < 1 then concat(abs(hour(now()) - hour(I.createdAt)), '시간 전')\n" +
                "           ELSE concat(datediff(now(), I.createdAt), '일 전')\n" +
                "        END AS period,\n" +
                "\n" +
                "       case\n" +
                "           when (select 1 from Likes inner join Users U on Likes.userIdx = U.userIdx\n" +
                "               where U.userIdx = ? and Likes.itemIdx = I.itemIdx)\n" +
                "               then 1\n" +
                "            ELSE 0\n" +
                "        END isLike,\n" +
                "       (select count(likeIdx) from Likes where I.itemIdx = Likes.itemIdx) AS likeCount,\n" +
                "\n" +
                "       I.itemCount,\n" +
                "       I.isCanExchange,\n" +
                "       I.isIncludeOrderTip,\n" +
                "       I.isUsed,\n" +
                "       I.itemContent,\n" +
                "       (select count(SaleViews.viewItemIdx) from SaleViews where SaleViews.itemIdx = I.itemIdx) AS viewCount,\n" +
                "       (select count(ChatRooms.chatRoomIdx) from ChatRooms where I.itemIdx = ChatRooms.itemIdx) AS chatCount,\n" +
                "       I.userIdx,\n" +
                "       (select count(F.followIdx) from Follows F where F.followingUserIdx = I.userIdx) AS followingCount,\n" +
                "       exists(select followerUserIdx from Follows F where F.followingUserIdx = I.userIdx AND F.followerUserIdx = ?) AS isFollowCheck,\n" +
                "       (select avg(C.star) from Comments C where C.sellUserIdx = I.userIdx) AS star,\n" +
                "       (select count(Items.itemIdx) from Items where Items.itemIdx IN (select Items.itemIdx from Items where Items.userIdx = I.userIdx)) AS storeItemCount,\n" +
                "       (select Users.profileImgUrl from Users where Users.userIdx = I.userIdx) AS storeImageUrl,\n" +
                "       I.isCanCheck,\n" +
                "       I.isSafePayment,\n" +
                "       (select count(Comments.buyUserIdx) from Comments where Comments.sellUserIdx = I.userIdx) AS commentCount\n" +
                "\n" +
                "from Items I\n" +
                "where I.itemIdx = ?\n" +
                "\n" +
                "order by I.createdAt DESC;";
        Object[] getItemInfoParams = new Object[]{buyUserIdx, buyUserIdx, itemIdx};

        return this.jdbcTemplate.queryForObject(getItemInfoQuery,
                (rs, rowNum) -> new GetItemInfoRes(
                        getImageUrl(itemIdx),
                        getTagList(itemIdx),
                        getStoreItemListRes(buyUserIdx, itemIdx),
                        getSimilarItemListRes(buyUserIdx, itemIdx),
                        getCommentList(itemIdx),
                        rs.getInt("cost"),
                        rs.getString("itemName"),
                        rs.getString("address"),
                        rs.getString("period"),
                        rs.getInt("isLike"),
                        rs.getInt("likeCount"),
                        rs.getInt("itemCount"),
                        rs.getInt("isCanExchange"),
                        rs.getInt("isIncludeOrderTip"),
                        rs.getInt("isUsed"),
                        rs.getString("itemContent"),
                        rs.getInt("viewCount"),
                        rs.getInt("chatCount"),
                        rs.getInt("userIdx"),
                        rs.getInt("followingCount"),
                        rs.getInt("isFollowCheck"),
                        rs.getFloat("star"),
                        rs.getInt("storeItemCount"),
                        rs.getString("storeImageUrl"),
                        rs.getInt("isCanCheck"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("commentCount")),
                getItemInfoParams);
    }


    // 이미지 리스트 가져오기
    public List<String> getImageUrl(int itemIdx){
        String getImageUrlQuery = "select Images.imageUrl\n" +
                "from Items I\n" +
                "inner join ItemImages II on I.itemIdx = II.itemIdx\n" +
                "inner join Images on Images.itemImageIdx = II.itemImageIdx = Images.itemImageIdx\n" +
                "where I.itemIdx = ?;";
        int getImageUrlParams = itemIdx;

        return this.jdbcTemplate.query(getImageUrlQuery,
                (rs, rowNum) -> new String(
                rs.getString("imageUrl")
        ), getImageUrlParams);
    }


    // 테그 리스트 가져오기
    public List<String> getTagList(int itemIdx){
        String getTagListQuery = "select T.tagName\n" +
                "from Items I\n" +
                "inner join Tags T on I.itemIdx = T.itemIdx\n" +
                "where I.itemIdx = ?";
        int getTagListParams = itemIdx;

        return this.jdbcTemplate.query(getTagListQuery,
                (rs, rowNum) -> new String(
                        rs.getString("tagName")),
                getTagListParams);
    }

    // 해당 상점의 상품 리스트 가져오기
    public List<GetItemListRes> getStoreItemListRes(int buyUserIdx, int itemIdx){
        String getStoreItemListQuery = "select I.itemIdx,\n" +
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
                "inner join Users U2 on I.userIdx = U2.userIdx\n" +
                "\n" +
                "\n" +
                "where I.userIdx = (select Items.userIdx from Items\n" +
                "                                         where Items.itemIdx = ?)\n" +
                "\n" +
                "\n" +
                "order by I.createdAt DESC;";
        Object[] getStoreItemListParams = new Object[]{buyUserIdx, itemIdx};

        return this.jdbcTemplate.query(getStoreItemListQuery,
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
                getStoreItemListParams);
    }


    // 비슷한 상품 가져오기(상품 idx를 통해 상품의 태그 중 브랜드, 카테고리 테그를 가지고 검색)
    public List<GetItemListRes> getSimilarItemListRes(int buyUserIdx, int itemIdx){
        String getSimilarItemListQuery = "select I.itemIdx,\n" +
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
                "\n" +
                "where I.itemIdx IN (select T2.itemIdx from Tags T2\n" +
                "                                      where T2.tagName IN\n" +
                "                                            (select Tags.tagName from Tags\n" +
                "                                                                 inner join Items I on Tags.itemIdx = I.itemIdx\n" +
                "                                                                 where I.itemIdx = ?))\n" +
                "AND I.itemIdx != ?\n" +
                "\n" +
                "order by I.createdAt DESC;";
        Object[] getSimilarItemListParams = new Object[]{buyUserIdx, itemIdx, itemIdx};

        return this.jdbcTemplate.query(getSimilarItemListQuery,
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
                getSimilarItemListParams);
    }




    // 후기 리스트 가져오기
    public List<Comments> getCommentList(int itemIdx){
        String getCommentQuery = "select C.star,\n" +
                "       C.buyUserIdx,\n" +
                "       C.isSafePayment,\n" +
                "       (select U2.userName from Users U2 where U2.userIdx = C.buyUserIdx) AS userName,\n" +
                "       case\n" +
                "           when datediff(now(), C.createdAt) < 1 then concat(abs(hour(now()) - hour(C.createdAt)), '시간 전')\n" +
                "           ELSE concat(datediff(now(), C.createdAt), '일 전')\n" +
                "        END AS period,\n" +
                "       C.commentText,\n" +
                "       (select I2.imageUrl from CommentImages CI\n" +
                "               inner join Images I2 on CI.commentImageIdx = I2.commentImageIdx\n" +
                "               where C.commentIdx = CI.commentIdx) AS imageUrl\n" +
                "\n" +
                "\n" +
                "from Items I\n" +
                "inner join Users U on I.userIdx = U.userIdx\n" +
                "inner join Comments C on U.userIdx = C.sellUserIdx\n" +
                "where I.itemIdx = ?";
        int getCommentParams = itemIdx;

        return this.jdbcTemplate.query(getCommentQuery,
                (rs, rowNum) -> new Comments(
                        rs.getFloat("star"),
                        rs.getInt("buyUserIdx"),
                        rs.getInt("isSafePayment"),
                        rs.getString("userName"),
                        rs.getString("period"),
                        rs.getString("commentText"),
                        rs.getString("imageUrl")),
                getCommentParams);
    }


}

