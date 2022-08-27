package com.example.demo.src.Comments;

import com.example.demo.src.Comments.model.GetStoreCommentRes;
import com.example.demo.src.Comments.model.GetUserCommentRes;
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


    public List<GetStoreCommentRes> getStoreComment(int userIdx) {
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

        return this.jdbcTemplate.query(getStoreCommentQuery,
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
    }

    public List<GetUserCommentRes> getUserComment(int userIdx) {
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

        return this.jdbcTemplate.query(getUserCommentQuery,
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
    }
}
