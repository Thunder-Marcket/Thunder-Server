package com.example.demo.src.brandFollows;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class BrandFollowDao {
    private JdbcTemplate jdbcTemplate;

    public int checkBrandFollows(int userIdx, int brandIdx) {
        String checkQuery = "select exists(select brand_followIdx from Brand_Follows where followerUserIdx = ? and followingBrandIdx = ?);";
        Object[] checkParams = new Object[]{userIdx, brandIdx};
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }

    public int createBrandFollows(int userIdx, int brandIdx) {
        String createQuery = "insert into Brand_Follows(followerUserIdx, followingBrandIdx) values (?, ?);";
        Object[] createParams = new Object[]{userIdx, brandIdx};
        return this.jdbcTemplate.update(createQuery, createParams);
    }

    public String checkBrandFollowsStatus(int userIdx, int brandIdx) {
        String checkQuery = "select status from Brand_Follows where followerUserIdx = ? and followingBrandIdx = ?;";
        Object[] checkParams = new Object[]{userIdx, brandIdx};
        return this.jdbcTemplate.queryForObject(checkQuery, String.class, checkParams);
    }

    public int modifyBrandFollowsStatus(int userIdx, int brandIdx, String status) {
        String modifyQuery = "update Brand_Follows set status = ?, updatedAt = now() where followerUserIdx = ? and followingBrandIdx = ?;";
        Object[] modifyParams = new Object[]{status, userIdx, brandIdx};
        return this.jdbcTemplate.update(modifyQuery, modifyParams);
    }
}
