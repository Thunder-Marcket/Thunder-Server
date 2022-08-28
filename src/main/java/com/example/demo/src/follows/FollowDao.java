package com.example.demo.src.follows;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
