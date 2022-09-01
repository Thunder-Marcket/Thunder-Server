package com.example.demo.src.follows;

import com.example.demo.src.follows.model.Following;
import com.example.demo.src.follows.model.FollowingUserItem;
import com.example.demo.src.follows.model.GetFollowerRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class FollowDao {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JdbcTemplate jdbcTemplate;

    public int checkFollowing(int userIdx, int followingUserIdx) {
        String checkFollowingQuery = "select exists(select followIdx from Follows where followerUserIdx = ? and followingUserIdx = ?);";
        Object[] checkFollowingParams = new Object[]{userIdx, followingUserIdx};
        return this.jdbcTemplate.queryForObject(checkFollowingQuery, int.class, checkFollowingParams);
    }

    public int follow(int userIdx, int followingUserIdx) {
        String checkFollowingQuery = "insert into Follows(followerUserIdx, followingUserIdx) values (?, ?)";
        Object[] checkFollowingParams = new Object[]{userIdx, followingUserIdx};
        return this.jdbcTemplate.update(checkFollowingQuery, checkFollowingParams);
    }

    public String checkFollowStatus(int userIdx, int followingUserIdx) {
        String checkFollowingQuery = "select status from Follows where followerUserIdx = ? and followingUserIdx = ?;\n";
        Object[] checkFollowingParams = new Object[]{userIdx, followingUserIdx};
        return this.jdbcTemplate.queryForObject(checkFollowingQuery, String.class, checkFollowingParams);
    }
    
    public int unfollow(int userIdx, int followingUserIdx) {
        String unFollowingQuery = "update Follows set status = 'disable' where followerUserIdx = ? and followingUserIdx = ?;\n";
        Object[] unFollowParams = new Object[]{userIdx, followingUserIdx};
        return this.jdbcTemplate.update(unFollowingQuery, unFollowParams);
    }

    public int modifyFollowStatus(int userIdx, int followingUserIdx) {
        String modifyFollowingQuery = "update Follows set status = 'enable' where followerUserIdx = ? and followingUserIdx = ?;\n";
        Object[] modifyFollowingParams = new Object[]{userIdx, followingUserIdx};
        return this.jdbcTemplate.update(modifyFollowingQuery, modifyFollowingParams);
    }

    public List<Following> getFollowings(int userIdx) {
        String getQuery =
                "select u.userIdx, i.itemIdx, u.profileImgUrl, u.userName\n" +
                "     , count(i.itemIdx) as itemCnt\n" +
                "     , count(followingUserIdx) as followerCnt\n" +
                "from Items i\n" +
                "    join(\n" +
                "        select userIdx, followingUserIdx, followerUserIdx, profileImgUrl, userName\n" +
                "        from Users u\n" +
                "            left join Follows F on u.userIdx = F.followingUserIdx\n" +
                "            where F.status = 'enable'\n" +
                "            group by F.followingUserIdx\n" +
                "    ) u on u.userIdx = i.userIdx\n" +
                "where u.followerUserIdx = ?\n" +
                "group by u.userIdx;";
        int getParams = userIdx;
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new Following(
                        rs.getInt("userIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("userName"),
                        rs.getInt("itemCnt"),
                        rs.getInt("followerCnt")),
                getParams);
    }

    public List<FollowingUserItem> getFollowingUserItemList(int followingUserIdx) {
        String getQuery =
                "select i.itemIdx, cost\n" +
                "    , (select Images.imageUrl from Images where Images.imageIdx = (select min(Images.imageIdx)\n" +
                "                                                                    from Images\n" +
                "                                                                        inner join ItemImages ii on Images.itemImageIdx = ii.itemImageIdx\n" +
                "                                                                        inner join Items i2 on ii.itemIdx = i2.itemIdx\n" +
                "                                                                    where i2.itemIdx = i.itemIdx)) as imageUrl\n" +
                "from Items i\n" +
                "where userIdx = ?;";
        int getParams = followingUserIdx;
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new FollowingUserItem(
                        rs.getString("imageUrl"),
                        rs.getInt("cost")),
                getParams);
    }

    public List<GetFollowerRes> getFollowers(int userIdx) {
        String getQuery =
                "select u.userIdx, profileImgUrl, userName\n" +
                "        , (select count(itemIdx) from Items where userIdx = followerUserIdx group by userIdx) as itemCnt\n" +
                "        , (select count(followerUserIdx) from Follows where followingUserIdx = F.followerUserIdx group by followingUserIdx) as followerCnt\n" +
                "from Users u\n" +
                "    left join Follows F on u.userIdx = F.followerUserIdx\n" +
                "where followingUserIdx = ?;";
        int getParams = userIdx;
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new GetFollowerRes(
                        rs.getInt("userIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("userName"),
                        rs.getInt("itemCnt"),
                        rs.getInt("followerCnt")),
                getParams);

    }
}
