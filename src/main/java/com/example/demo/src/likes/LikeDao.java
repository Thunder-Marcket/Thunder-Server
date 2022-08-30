package com.example.demo.src.likes;

import com.example.demo.src.likes.model.GetLikeItemRes;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class LikeDao {
    private JdbcTemplate jdbcTemplate;

    // 찜 목록 조회
    public List<GetLikeItemRes> getLikeItems(int userIdx) {
        String getLikeItemsQuery =
                "select i.itemIdx, itemName, concat(cost, '원') as cost, userName, isSafePayment,\n" +
                        "       case when timestampdiff(second , i.updatedAt, current_timestamp) <60\n" +
                        "        then concat(timestampdiff(second, i.updatedAt, current_timestamp),' 초 전')\n" +
                        "        when timestampdiff(minute , i.updatedAt, current_timestamp) <60\n" +
                        "        then concat(timestampdiff(minute, i.updatedAt, current_timestamp),' 분 전')\n" +
                        "        when timestampdiff(hour , i.updatedAt, current_timestamp) <24\n" +
                        "        then concat(timestampdiff(hour, i.updatedAt, current_timestamp),' 시간 전')\n" +
                        "        else concat(datediff( current_timestamp, i.updatedAt),' 일 전')\n" +
                        "    end                                      as uploadTime,\n" +
                        "       l.status,\n" +
                        "       (select Images.imageUrl from Images where Images.imageIdx = (select min(Images.imageIdx)\n" +
                        "                                                                    from Images\n" +
                        "                                                                        inner join ItemImages ii on Images.itemImageIdx = ii.itemImageIdx\n" +
                        "                                                                        inner join Items i2 on ii.itemIdx = i2.itemIdx\n" +
                        "                                                                    where i2.itemIdx = i.itemIdx)) as imageUrl\n" +
                        "from (\n" +
                        "     select itemIdx, itemName, cost, updatedAt, createdAt, userIdx, isSafePayment\n" +
                        "    from Items\n" +
                        ") i\n" +
                        "join (\n" +
                        "    select userIdx, userName\n" +
                        "    from Users\n" +
                        ") u on u.userIdx = i.userIdx\n" +
                        "join (\n" +
                        "    select userIdx, itemIdx, status\n" +
                        "    from Likes\n" +
                        "    where userIdx = ? and status = 'enable'\n" +
                        ") l on l.itemIdx = i.itemIdx";
        int getLikeItemsParams = userIdx;
        return this.jdbcTemplate.query(getLikeItemsQuery,
                (rs, rowNum) -> new GetLikeItemRes(
                        rs.getInt("itemIdx"),
                        rs.getString("itemName"),
                        rs.getString("cost"),
                        rs.getString("userName"),
                        rs.getString("status"),
                        rs.getInt("isSafePayment"),
                        rs.getString("uploadTime"),
                        rs.getString("imageUrl")),
                getLikeItemsParams);
    }

    public int checkLikes(int userIdx, int itemIdx) {
        String checkQuery = "select exists(select likeIdx from Likes where userIdx = ? and itemIdx = ?);\n";
        Object[] checkParams = new Object[]{userIdx, itemIdx};
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }

    public int createLikes(int userIdx, int itemIdx) {
        String createQuery = "insert into Likes(userIdx, itemIdx) values(?, ?);";
        Object[] createParams = new Object[]{userIdx, itemIdx};
        return this.jdbcTemplate.update(createQuery, createParams);
    }

    public String checkLikesStatus(int userIdx, int itemIdx) {
        String checkQuery = "select status from Likes where userIdx = ? and itemIdx = ?;";
        Object[] checkParams = new Object[]{userIdx, itemIdx};
        return this.jdbcTemplate.queryForObject(checkQuery, String.class, checkParams);
    }

    public int modifyLikeStatus(int userIdx, int itemIdx, String status) {
        String modifyQuery = "update Likes set status = ?, updatedAt = now() where userIdx = ? and itemIdx = ?;";
        Object[] modifyParams = new Object[]{status, userIdx, itemIdx};
        return this.jdbcTemplate.update(modifyQuery, modifyParams);
    }
}
