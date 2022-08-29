
package com.example.demo.src.Items;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Items.model.*;
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
                "       concat(FORMAT(I.cost, 0), '원') AS cost,\n" +
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
                "       I.isAdItem,\n" +
                "       case\n" +
                "           when I.status = 'sold' then '판매 완료'\n" +
                "           ELSE '판매 중'\n" +
                "       END AS status\n" +
                "\n" +
                "from Items I\n" +
                "\n" +
                "order by I.createdAt DESC;";
        int getItemParams = userIdx;


        return this.jdbcTemplate.query(getItemQuery,
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
                        rs.getInt("isAdItem"),
                        rs.getString("status")),
                getItemParams);
    }


    // 검색어로 상품 가져오기
    public List<GetItemListRes> getSearchItems(int userIdx, String search) throws BaseException {
        String insertSearchQuery = "insert into Searchs (searchText, userIdx) VALUES (?,?);";
        Object[] insertSearchParams = new Object[]{search, userIdx};

        this.jdbcTemplate.update(insertSearchQuery, insertSearchParams);




        String getSearchItemQuery = "select I.itemIdx,\n" +
                "       concat(FORMAT(I.cost, 0), '원') AS cost,\n" +
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
                "       I.isAdItem,\n" +
                "       case\n" +
                "           when I.status = 'sold' then '판매 완료'\n" +
                "           ELSE '판매 중'\n" +
                "       END AS status\n" +
                "\n" +
                "from Items I\n" +
                "\n" +
                "where instr(I.itemName, ?) or I.itemIdx IN (select T2.itemIdx from Tags T2\n" +
                "                                      where T2.tagName IN\n" +
                "                                            (select Tags.tagName from Tags\n" +
                "                                                                 inner join Items I on Tags.itemIdx = I.itemIdx\n" +
                "                                                                 where T2.tagName = ?))\n" +
                "\n" +
                "order by I.createdAt DESC;";
        Object[] getSearchItemParam = new Object[]{userIdx, search, search};

        return this.jdbcTemplate.query(getSearchItemQuery,
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
                        rs.getInt("isAdItem"),
                        rs.getString("status")),
                getSearchItemParam);

    }


    // 상세 상품 정보 가져오기
    public GetItemInfoRes getItemInfo(int buyUserIdx, int itemIdx) {
        String getItemInfoQuery = "select concat(FORMAT(I.cost, 0), '원') AS cost,\n" +
                "       I.itemName,\n" +
                "       case\n" +
                "           when I.address is null then '지역정보 없음'\n" +
                "           ELSE I.address\n" +
                "       END AS address,\n" +
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
                "       concat(I.itemCount, '개') AS itemCount,\n" +
                "       I.isCanExchange,\n" +
                "       I.isIncludeOrderTip,\n" +
                "       I.isUsed,\n" +
                "       case\n" +
                "           when I.itemContent is null then '상품 내용 소개 없음'\n" +
                "            ELSE I.itemContent\n" +
                "        END AS itemContent,\n" +
                "       (select count(SaleViews.viewItemIdx) from SaleViews where SaleViews.itemIdx = I.itemIdx) AS viewCount,\n" +
                "       (select count(ChatRooms.chatRoomIdx) from ChatRooms where I.itemIdx = ChatRooms.itemIdx) AS chatCount,\n" +
                "       I.userIdx,\n" +
                "       (select count(F.followIdx) from Follows F where F.followingUserIdx = I.userIdx) AS followingCount,\n" +
                "       exists(select followerUserIdx from Follows F where F.followingUserIdx = I.userIdx AND F.followerUserIdx = ?) AS isFollowCheck,\n" +
                "       (select avg(C.star) from Comments C where C.sellUserIdx = I.userIdx) AS star,\n" +
                "       (select count(Items.itemIdx) from Items where Items.itemIdx IN (select Items.itemIdx from Items where Items.userIdx = I.userIdx)) AS storeItemCount,\n" +
                "       (select\n" +
                "            case\n" +
                "                when Users.profileImgUrl is null then '이미지 없음'\n" +
                "                ELSE Users.profileImgUrl\n" +
                "            END from Users where Users.userIdx = I.userIdx) AS storeImageUrl,\n" +
                "       I.isCanCheck,\n" +
                "       I.isSafePayment,\n" +
                "       (select count(Comments.buyUserIdx) from Comments where Comments.sellUserIdx = I.userIdx) AS commentCount,\n" +
                "       case\n" +
                "           when I.status = 'sold' then '판매 완료'\n" +
                "           ELSE '판매 중'\n" +
                "       END AS status\n" +
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
                        //getStoreItemListRes(buyUserIdx, itemIdx),
                        getSimilarItemListRes(buyUserIdx, itemIdx),
                        getCommentList(itemIdx),
                        rs.getString("cost"),
                        rs.getString("itemName"),
                        rs.getString("address"),
                        rs.getString("period"),
                        rs.getInt("isLike"),
                        rs.getInt("likeCount"),
                        rs.getString("itemCount"),
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
                        rs.getInt("commentCount"),
                        rs.getString("status")),
                getItemInfoParams);
    }


    // 이미지 리스트 가져오기
    public List<String> getImageUrl(int itemIdx){
        String getImageUrlQuery = "select Images.imageUrl\n" +
                "from Items I\n" +
                "inner join ItemImages II on I.itemIdx = II.itemIdx\n" +
                "inner join Images on Images.itemImageIdx = II.itemImageIdx\n" +
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
                "       concat(FORMAT(I.cost, 0), '원') AS cost,\n" +
                "       I.itemName,\n" +
                "       case\n" +
                "           when I.address is null then '지역정보 없음'\n" +
                "           ELSE I.address\n" +
                "       END AS address,\n" +
                "       case\n" +
                "           when datediff(now(), I.createdAt) < 1 then concat(abs(hour(now()) - hour(I.createdAt)), '시간 전')\n" +
                "           ELSE concat(datediff(now(), I.createdAt), '일 전')\n" +
                "        END AS period,\n" +
                "       case\n" +
                "           when (select Images.imageUrl from Images\n" +
                "                 where Images.imageIdx = (select min(Images.imageIdx) from Images\n" +
                "                                                                      inner join ItemImages II on Images.itemImageIdx = II.itemImageIdx\n" +
                "                                                                      inner join Items I2 on II.itemIdx = I2.itemIdx\n" +
                "                                                                      where I2.itemIdx = I.itemIdx)) is null\n" +
                "                then '이미지 없음'\n" +
                "            ELSE (select Images.imageUrl from Images\n" +
                "                 where Images.imageIdx = (select min(Images.imageIdx) from Images\n" +
                "                                                                      inner join ItemImages II on Images.itemImageIdx = II.itemImageIdx\n" +
                "                                                                      inner join Items I2 on II.itemIdx = I2.itemIdx\n" +
                "                                                                      where I2.itemIdx = I.itemIdx))\n" +
                "       END AS imageUrl,\n" +
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
                "       I.isAdItem,\n" +
                "       case\n" +
                "           when I.status = 'sold' then '판매 완료'\n" +
                "           ELSE '판매 중'\n" +
                "       END AS status\n" +
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
                        rs.getString("cost"),
                        rs.getString("itemName"),
                        rs.getString("address"),
                        rs.getString("period"),
                        rs.getString("imageUrl"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("isLike"),
                        rs.getInt("likeCnt"),
                        rs.getInt("isCanCheck"),
                        rs.getInt("isAdItem"),
                        rs.getString("status")),
                getStoreItemListParams);
    }


    // 비슷한 상품 가져오기(상품 idx를 통해 상품의 태그 중 브랜드, 카테고리 테그를 가지고 검색)
    public List<GetItemListRes> getSimilarItemListRes(int buyUserIdx, int itemIdx){
        String getSimilarItemListQuery = "select I.itemIdx,\n" +
                "       concat(FORMAT(I.cost, 0), '원') AS cost,\n" +
                "       I.itemName,\n" +
                "       case\n" +
                "           when I.address is null then '지역정보 없음'\n" +
                "           ELSE I.address\n" +
                "       END AS address,\n" +
                "       case\n" +
                "           when datediff(now(), I.createdAt) < 1 then concat(abs(hour(now()) - hour(I.createdAt)), '시간 전')\n" +
                "           ELSE concat(datediff(now(), I.createdAt), '일 전')\n" +
                "        END AS period,\n" +
                "\n" +
                "      case\n" +
                "          when (select\n" +
                "            Images.imageUrl\n" +
                "             from Images\n" +
                "                 where Images.imageIdx = (select min(Images.imageIdx) from Images\n" +
                "                                                                      inner join ItemImages II on Images.itemImageIdx = II.itemImageIdx\n" +
                "                                                                      inner join Items I2 on II.itemIdx = I2.itemIdx\n" +
                "                                                                      where I2.itemIdx = I.itemIdx)) is null then '이미지 없음'\n" +
                "          ELSE (select\n" +
                "            Images.imageUrl\n" +
                "             from Images\n" +
                "                 where Images.imageIdx = (select min(Images.imageIdx) from Images\n" +
                "                                                                      inner join ItemImages II on Images.itemImageIdx = II.itemImageIdx\n" +
                "                                                                      inner join Items I2 on II.itemIdx = I2.itemIdx\n" +
                "                                                                      where I2.itemIdx = I.itemIdx))\n" +
                "        END imageUrl,\n" +
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
                "       I.isAdItem,\n" +
                "       case\n" +
                "           when I.status = 'sold' then '판매 완료'\n" +
                "           ELSE '판매 중'\n" +
                "       END AS status\n" +
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
                        rs.getString("cost"),
                        rs.getString("itemName"),
                        rs.getString("address"),
                        rs.getString("period"),
                        rs.getString("imageUrl"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("isLike"),
                        rs.getInt("likeCnt"),
                        rs.getInt("isCanCheck"),
                        rs.getInt("isAdItem"),
                        rs.getString("status")),
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
                "       case\n" +
                "           when (select I2.imageUrl from CommentImages CI\n" +
                "                                    inner join Images I2 on CI.commentImageIdx = I2.commentImageIdx\n" +
                "                                    where C.commentIdx = CI.commentIdx) is null\n" +
                "                then '이미지 없음'\n" +
                "           ELSE (select I2.imageUrl from CommentImages CI\n" +
                "                                    inner join Images I2 on CI.commentImageIdx = I2.commentImageIdx\n" +
                "                                    where C.commentIdx = CI.commentIdx)\n" +
                "       END AS imageUrl\n" +
                "\n" +
                "\n" +
                "from Items I\n" +
                "inner join Users U on I.userIdx = U.userIdx\n" +
                "inner join Comments C on U.userIdx = C.sellUserIdx\n" +
                "where I.itemIdx = ?;";
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


    public int createItem(PostItemReq postItemReq) {
        // item 생성
        String createItemQuery = "insert into Items (itemName, itemContent, cost, isIncludeOrderTip, itemCount, isCanExchange, isUsed, isSafePayment, userIdx, address, categoryIdx, subCategoryIdx, subSubcategoryIdx)\n" +
                "    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
        Object[] createItemParam;

        if(postItemReq.getSubCategoryIdx() == 0){
            createItemParam = new Object[]{
                    postItemReq.getItemName(),
                    postItemReq.getItemContent(),
                    postItemReq.getItemCost(),
                    postItemReq.getIsIncludeOrderTip(),
                    postItemReq.getItemCount(),
                    postItemReq.getIsCanExchange(),
                    postItemReq.getIsUsed(),
                    postItemReq.getIsSafePayment(),
                    postItemReq.getUserIdx(),
                    postItemReq.getAddress(),
                    postItemReq.getCategoryIdx(),
                    null,
                    null,
            };
        }
        else if(postItemReq.getSubSubcategoryIdx() == 0){
            createItemParam = new Object[]{
                    postItemReq.getItemName(),
                    postItemReq.getItemContent(),
                    postItemReq.getItemCost(),
                    postItemReq.getIsIncludeOrderTip(),
                    postItemReq.getItemCount(),
                    postItemReq.getIsCanExchange(),
                    postItemReq.getIsUsed(),
                    postItemReq.getIsSafePayment(),
                    postItemReq.getUserIdx(),
                    postItemReq.getAddress(),
                    postItemReq.getCategoryIdx(),
                    postItemReq.getSubCategoryIdx(),
                    null,
            };
        }
        else{
            createItemParam = new Object[]{
                    postItemReq.getItemName(),
                    postItemReq.getItemContent(),
                    postItemReq.getItemCost(),
                    postItemReq.getIsIncludeOrderTip(),
                    postItemReq.getItemCount(),
                    postItemReq.getIsCanExchange(),
                    postItemReq.getIsUsed(),
                    postItemReq.getIsSafePayment(),
                    postItemReq.getUserIdx(),
                    postItemReq.getAddress(),
                    postItemReq.getCategoryIdx(),
                    postItemReq.getSubCategoryIdx(),
                    postItemReq.getSubSubcategoryIdx(),
            };
        }

        this.jdbcTemplate.update(createItemQuery, createItemParam);

        String lastInsertIdQuery = "select last_insert_id()";
        int itemIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);


        // item과 연결된 이미지 생성
        createItemImage(itemIdx, postItemReq.getImageUrlList());


        // tag 생성
        for(int i = 0; i < postItemReq.getTagNameList().size(); i++){
            createItemTag(itemIdx, postItemReq.getTagNameList().get(i), 0);
        }

        if(getCategoryName(postItemReq.getCategoryIdx()) != null){
            createItemTag(itemIdx, getCategoryName(postItemReq.getCategoryIdx()), 1);
        }
        if(getSubCategoryName(postItemReq.getSubCategoryIdx()) != null){
            createItemTag(itemIdx, getSubCategoryName(postItemReq.getSubCategoryIdx()), 1);
        }
        if(getSubSubCategoryName(postItemReq.getSubSubcategoryIdx()) != null){
            createItemTag(itemIdx, getSubSubCategoryName(postItemReq.getSubSubcategoryIdx()), 1);
        }




       return itemIdx;
    }

    public void createItemImage(int itemIdx, List<String> imageUrlList){
        String createItemImageUrlQuery = "insert into ItemImages (itemIdx) VALUES (?)";
        int createItemImageUrlParam = itemIdx;

        this.jdbcTemplate.update(createItemImageUrlQuery, createItemImageUrlParam);

        String lastInsertIdQuery = "select last_insert_id()";
        int itemImageIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);


        String createItemImageQuery = "insert into Images (imageUrl, itemImageIdx) values (?, ?);";
        Object[] createItemImageParam;

        for(int i =0 ; i < imageUrlList.size(); i++){
            createItemImageParam = new Object[]{imageUrlList.get(i), itemImageIdx};
            this.jdbcTemplate.update(createItemImageQuery, createItemImageParam);
        }
    }

    public String getCategoryName(int categoryIdx){
        String getCategoryExistQuery = "select exists(select C.categoryName from Categorys C where C.categoryIdx = ?);";

        if(this.jdbcTemplate.queryForObject(getCategoryExistQuery, int.class,categoryIdx) == 1){
            String getItemCategoryNameQuery = "select C.categoryName from Categorys C where C.categoryIdx = ?; ";

            return this.jdbcTemplate.queryForObject(getItemCategoryNameQuery, String.class, categoryIdx);
        }
        else{
            return null;
        }
    }

    public String getSubCategoryName(int categoryIdx){
        String getSubCategoryExistQuery = "select exists(select SC.categoryName from SubCategory SC where SC.subCategoryIdx = ?);";

        if(this.jdbcTemplate.queryForObject(getSubCategoryExistQuery, int.class, categoryIdx) == 1) {
            String getItemCategoryNameQuery = "select SC.categoryName from SubCategory SC where SC.subCategoryIdx = ?;";

            return this.jdbcTemplate.queryForObject(getItemCategoryNameQuery, String.class, categoryIdx);
        }
        else{
            return null;
        }
    }


    public String getSubSubCategoryName(int categoryIdx){
        String getSubSubCategoryExistQuery = "select exists(select SSC.categoryName  from SubSubCategory SSC where SSC.subSubcategoryIdx = ?);";

        if (this.jdbcTemplate.queryForObject(getSubSubCategoryExistQuery, int.class, categoryIdx) == 1) {
            String getItemCategoryNameQuery = "select SSC.categoryName from SubSubCategory SSC where SSC.subSubcategoryIdx = ?;";
            return this.jdbcTemplate.queryForObject(getItemCategoryNameQuery, String.class, categoryIdx);
        }
        else{
            return null;
        }
    }

    public void createItemTag(int itemIdx, String tagName, int isCategory){
        String createItemTagQuery = "insert into Tags (tagName, itemIdx, isCategory) VALUES (?,?,?);";
        Object[] createItemTagParam = new Object[]{tagName, itemIdx, isCategory};


        this.jdbcTemplate.update(createItemTagQuery, createItemTagParam);
    }

    public List<GetRegistItem> getRegistItem(int userIdx) {
        String getRegistItemQuery = "select I.itemIdx,\n" +
                "       concat(FORMAT(I.cost, 0), '원') AS cost,\n" +
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
                "\n" +
                "\n" +
                "       (select count(likeIdx) from Likes where I.itemIdx = Likes.itemIdx) AS likeCnt,\n" +
                "       I.isCanCheck,\n" +
                "\n" +
                "       case\n" +
                "           when I.status = 'sold' then '판매 완료'\n" +
                "           ELSE '판매 중'\n" +
                "       END AS status\n" +
                "\n" +
                "from Items I\n" +
                "where I.userIdx = ?\n" +
                "\n" +
                "\n" +
                "order by I.createdAt DESC;";
        int getRegistItemParam = userIdx;

        return this.jdbcTemplate.query(getRegistItemQuery,
                (rs, rowNum) -> new GetRegistItem(
                        rs.getInt("itemIdx"),
                        rs.getString("cost"),
                        rs.getString("itemName"),
                        rs.getString("address"),
                        rs.getString("period"),
                        rs.getString("imageUrl"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("likeCnt"),
                        rs.getInt("isCanCheck"),
                        rs.getString("status")
                ),
                getRegistItemParam);
    }

    public int getExistItem(int itemIdx) {
        String getExistItemQuery = "select exists(select I.itemIdx from Items I where I.itemIdx = ?);";
        int getExistItemParam = itemIdx;

        return this.jdbcTemplate.queryForObject(getExistItemQuery, int.class, getExistItemParam);
    }

    public PatchItemRes modifyItem(PatchItemReq patchItemReq, int itemIdx) {
        String modifyItemQuery = "update Items set itemName = ?,\n" +
                "                 address = ?,\n" +
                "                 itemContent = ?,\n" +
                "                 cost = ?,\n" +
                "                 itemCount =?,\n" +
                "                 isIncludeOrderTip = ?,\n" +
                "                 isSafePayment = ?,\n" +
                "                 isUsed = ?,\n" +
                "                 isCanExchange = ?\n" +
                "\n" +
                "             where itemIdx = ?;";
        Object[] modifyItemParams = new Object[]{
                patchItemReq.getItemName(),
                patchItemReq.getItemContent(),
                patchItemReq.getItemCost(),
                patchItemReq.getIsIncludeOrderTip(),
                patchItemReq.getItemCount(),
                patchItemReq.getIsCanExchange(),
                patchItemReq.getIsUsed(),
                patchItemReq.getIsSafePayment(),
                patchItemReq.getAddress(),
                itemIdx
        };

        this.jdbcTemplate.update(modifyItemQuery, modifyItemParams);
        return new PatchItemRes(itemIdx);
    }

    public int existItemImage(int itemIdx){
        String existItemImageQuery = "select exists(select ItemImages.itemImageIdx from ItemImages where ItemImages.itemIdx = ?);";

        return this.jdbcTemplate.queryForObject(existItemImageQuery, int.class, itemIdx);
    }

    public int getItemImageIdx(int itemIdx) throws BaseException{
        if(existItemImage(itemIdx) == 0){
            throw new BaseException(BaseResponseStatus.POST_ITEMS_NEED_IMAGES);
        }


        String getItemImageQuery = "select\n" +
                "    II.itemImageIdx\n" +
                "from ItemImages II where II.itemIdx = ?";
        int getItemImageParam = itemIdx;

        return this.jdbcTemplate.queryForObject(getItemImageQuery, int.class, getItemImageParam);
    }

    public void modifyItemImage(PatchItemReq patchItemReq, int itemIdx) throws BaseException {
        ;
    }


}

