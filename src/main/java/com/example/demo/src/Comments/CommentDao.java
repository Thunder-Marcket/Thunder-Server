package com.example.demo.src.Comments;

import com.example.demo.src.Comments.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommentDao {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public GetStoreCommentListRes getStoreComment(int userIdx) {
        String getStoreCommentQuery = "select\n" +
                "    C.commentIdx,\n" +
                "    C.buyUserIdx,\n" +
                "    C.star,\n" +
                "    (select Users.userName from Users where Users.userIdx = C.buyUserIdx) AS userName,\n" +
                "    case\n" +
                "        when datediff(now(), C.createdAt) < 1 then concat(abs(hour(now()) - hour(C.createdAt)), '시간 전')\n" +
                "        ELSE concat(datediff(now(), C.createdAt), '일 전')\n" +
                "    END AS period,\n" +
                "    C.commentText,\n" +
                "    case\n" +
                "           when (select I2.imageUrl from CommentImages CI\n" +
                "                                    inner join Images I2 on CI.commentImageIdx = I2.commentImageIdx\n" +
                "                                    where C.commentIdx = CI.commentIdx) is null\n" +
                "                then '이미지 없음'\n" +
                "           ELSE (select I2.imageUrl from CommentImages CI\n" +
                "                                    inner join Images I2 on CI.commentImageIdx = I2.commentImageIdx\n" +
                "                                    where C.commentIdx = CI.commentIdx)\n" +
                "    END AS commentImageUrl,\n" +
                "    C.isSafePayment\n" +
                "\n" +
                "from Comments C\n" +
                "inner join Users U on C.sellUserIdx = U.userIdx\n" +
                "where U.userIdx = ?;";
        int getStoreCommentParam = userIdx;

        List<GetStoreCommentRes> getStoreCommentRes = this.jdbcTemplate.query(getStoreCommentQuery,
                (rs, rowNum) -> new GetStoreCommentRes(
                        rs.getInt("commentIdx"),
                        rs.getInt("buyUserIdx"),
                        rs.getFloat("star"),
                        rs.getString("userName"),
                        rs.getString("period"),
                        rs.getString("commentText"),
                        rs.getString("commentImageUrl"),
                        rs.getInt("isSafePayment")
                ),
                getStoreCommentParam);


        return new GetStoreCommentListRes(getStoreCommentCount(userIdx), getStoreCommentRes);
    }


    public int getStoreCommentCount(int userIdx){
        String getExistCommentQuery = "select exists(select C.commentIdx from Comments C inner join Users U on C.sellUserIdx = ?);";
        int getExistCommentParam = userIdx;

        int count = 0;

        if(this.jdbcTemplate.queryForObject(getExistCommentQuery, int.class, getExistCommentParam) == 0){
            return count;
        }
        else{
            String getCommentCountQuery = "select count(Comments.buyUserIdx) AS commentCount from Comments where Comments.sellUserIdx = ? ;";
            int getCommentParam = userIdx;

            count = this.jdbcTemplate.queryForObject(getCommentCountQuery,
                    int.class, getCommentParam);
            return count;
        }

    }


    public GetUserCommentListRes getUserComment(int userIdx) {
        String getUserCommentQuery = "select\n" +
                "    C.commentIdx,\n" +
                "    C.sellUserIdx,\n" +
                "    C.star,\n" +
                "    (select Users.userName from Users where Users.userIdx = C.sellUserIdx) AS userName,\n" +
                "    case\n" +
                "        when datediff(now(), C.createdAt) < 1 then concat(abs(hour(now()) - hour(C.createdAt)), '시간 전')\n" +
                "        ELSE concat(datediff(now(), C.createdAt), '일 전')\n" +
                "    END AS period,\n" +
                "    C.commentText,\n" +
                "    case\n" +
                "           when (select I2.imageUrl from CommentImages CI\n" +
                "                                    inner join Images I2 on CI.commentImageIdx = I2.commentImageIdx\n" +
                "                                    where C.commentIdx = CI.commentIdx) is null\n" +
                "                then '이미지 없음'\n" +
                "           ELSE (select I2.imageUrl from CommentImages CI\n" +
                "                                    inner join Images I2 on CI.commentImageIdx = I2.commentImageIdx\n" +
                "                                    where C.commentIdx = CI.commentIdx)\n" +
                "    END AS commentImageUrl,\n" +
                "    C.isSafePayment\n" +
                "\n" +
                "from Comments C\n" +
                "inner join Users U on C.buyUserIdx = U.userIdx\n" +
                "where U.userIdx = ?;";
        int getUserCommentParams = userIdx;

        List<GetUserCommentRes> getUserCommentRes = this.jdbcTemplate.query(getUserCommentQuery,
                (rs, rowNum) -> new GetUserCommentRes(
                        rs.getInt("commentIdx"),
                        rs.getInt("sellUserIdx"),
                        rs.getFloat("star"),
                        rs.getString("userName"),
                        rs.getString("period"),
                        rs.getString("commentText"),
                        rs.getString("commentImageUrl"),
                        rs.getInt("isSafePayment")
                ),
                getUserCommentParams);

        return new GetUserCommentListRes(getUserCommentCount(userIdx), getUserCommentRes);
    }

    public int getUserCommentCount(int userIdx){
        String getExistCommentQuery = "select exists(select C.commentIdx from Comments C inner join Users U on C.buyUserIdx = ?);";
        int getExistCommentParam = userIdx;

        if(this.jdbcTemplate.queryForObject(getExistCommentQuery, int.class, getExistCommentParam) == 0){
            return 0;
        }

        String getCommentCountQuery = "select count(Comments.sellUserIdx) AS commentCount from Comments where Comments.buyUserIdx= ? ;";
        int getCommentParam = userIdx;

        return this.jdbcTemplate.queryForObject(getCommentCountQuery,
                int.class, getCommentParam);
    }


    public PostCommentRes createComment(PostCommentReq postCommentReq, int orderIdx) {
        logger.warn("createComment in");
        CommentItem commentItem = getCommentItem(orderIdx);
        logger.warn("getCommentItem out");

        String createCommentQuery = "insert into Comments (star, buyUserIdx, sellUserIdx, commentText, isSafePayment) VALUES (?,?,?,?,?);";
        Object[] createCommentParams = new Object[]{
                postCommentReq.getStar(),
                postCommentReq.getBuyUserIdx(),
                commentItem.getSellUserIdx(),
                postCommentReq.getCommentText(),
                commentItem.getIsSafePayment()
        };

        this.jdbcTemplate.update(createCommentQuery, createCommentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int commentIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        int commentImageIdx = createCommentImage(commentIdx);

        for(int i = 0; i < postCommentReq.getCommentImageList().size(); i++){
            createImage(commentImageIdx, postCommentReq.getCommentImageList().get(i));
        }
        logger.warn("createComment out");
        return new PostCommentRes(this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class));
    }

    public int existOrder(PostCommentReq postCommentReq, int orderIdx) {
        logger.warn("existOrder in");
        String existOrderQuery = "select exists(select O.orderIdx from Orders O where O.buyUserIdx = ? and O.orderIdx = ?);";
        Object[] existOrderParam = new Object[]{postCommentReq.getBuyUserIdx(), orderIdx};

        logger.warn("existOrder out");
        return this.jdbcTemplate.queryForObject(existOrderQuery, int.class, existOrderParam);
    }



    public CommentItem getCommentItem(int orderIdx){
        logger.warn("getCommentItem in");

        String commentItemQuery = "select\n" +
                "    I.userIdx,\n" +
                "    I.isSafePayment\n" +
                "from Orders O\n" +
                "inner join Items I on O.itemIdx = I.itemIdx where O.orderIdx = ?;";
        Object[] commentItemParams = new Object[]{orderIdx};

        return this.jdbcTemplate.queryForObject(commentItemQuery,
                (rs, rowNum) -> new CommentItem(
                        rs.getInt("userIdx"),
                        rs.getInt("isSafePayment")
                ), commentItemParams);
    }


    public int createCommentImage(int commentIdx){
        logger.warn("createCommentImage in");
        String createImageQuery = "insert into CommentImages (commentIdx) VALUES (?);";
        int createImageParam = commentIdx;

        this.jdbcTemplate.update(createImageQuery, createImageParam);

        logger.warn("createCommentImage out");
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


    public void createImage(int commentImageIdx, String imageUrl){
        logger.warn("createImage in");

        String createImageQuery = "insert into Images (commentImageIdx, imageUrl)  VALUES (?,?);";
        Object[] createImageParam = new Object[]{commentImageIdx, imageUrl};

        this.jdbcTemplate.update(createImageQuery, createImageParam);
        logger.warn("createImage out");
    }


//    public int existComment(PostCommentReq postCommentReq, int orderIdx) {
//        String existCommentQuery = "";
//        Object[] existCommentParam = new Object[]{};
//
//        return this.jdbcTemplate.queryForObject(existCommentQuery, int.class, existCommentParam);
//    }
}
