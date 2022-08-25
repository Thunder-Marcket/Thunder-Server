
package com.example.demo.src.users;


import com.example.demo.src.users.model.*;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

//    public List<GetUserRes> getUsers(){
//        String getUsersQuery = "select * from UserInfo";
//        return this.jdbcTemplate.query(getUsersQuery,
//                (rs,rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password"))
//                );
//    }


    public GetUserRes getUser(int userIdx) {
        String getUserQuery =
                "select profileImgUrl, userName, grade, isSelfVerification\n" +
                "     , case when likeCnt > 0 then likeCnt else 0 end as likeCnt\n" +
                "     , case when commentCnt > 0 then commentCnt else 0 end as commentCnt\n" +
                "     , case when followerCnt > 0 then followerCnt else 0 end as followerCnt\n" +
                "     , case when followingCnt > 0 then followingCnt else 0 end as followingCnt\n" +
                "\n" +
                "from (\n" +
                "    select profileImgUrl, userName, grade, isSelfVerification, userIdx\n" +
                "    from Users\n" +
                "    where userIdx = ? \n" +
                ") u\n" +
                "left join (\n" +
                "    select count(likeIdx) as likeCnt, l.userIdx\n" +
                "    from Likes l\n" +
                "        join Items i on i.itemIdx = l.itemIdx\n" +
                "    group by l.userIdx\n" +
                ") ll on u.userIdx = ll.userIdx\n" +
                "left join (\n" +
                "    select count(commentIdx) as commentCnt, sellUserIdx\n" +
                "    from Comments c\n" +
                "    group by c.sellUserIdx\n" +
                ") c on c.sellUserIdx = u.userIdx\n" +
                "left join (\n" +
                "    select count(followerUserIdx) as followerCnt, followingUserIdx\n" +
                "    from Follows f\n" +
                "    group by f.followingUserIdx\n" +
                ") f on f.followingUserIdx = u.userIdx\n" +
                "left join (\n" +
                "    select count(followingUserIdx) as followingCnt, followerUserIdx\n" +
                "    from Follows f\n" +
                "    group by f.followerUserIdx\n" +
                ") ff on ff.followerUserIdx = u.userIdx;";
        int getUserParam = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("profileImgUrl"),
                        rs.getString("userName"),
                        rs.getInt("grade"),
                        rs.getInt("isSelfVerification"),
                        rs.getInt("likeCnt"),
                        rs.getInt("commentCnt"),
                        rs.getInt("followerCnt"),
                        rs.getInt("followingCnt")),
                getUserParam);
    }

    public PatchUserRes getModifiedUser(int userIdx){
        String getUserQuery =
                "select userIdx, userName, profileImgUrl, status\n" +
                "from Users\n" +
                "where userIdx = ?;";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new PatchUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("profileImgUrl"),
                        rs.getString("status")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into Users(userName, phoneNumber, birth) VALUES (?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getPhoneNumber(), postUserReq.getBirth()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


    public int checkPhoneNum(String phoneNum) {
        String checkPhoneNumQuery = "select exists(select phoneNumber from Users where phoneNumber = ?)";
        String checkPhoneNumParams = phoneNum;
        return this.jdbcTemplate.queryForObject(checkPhoneNumQuery,
                int.class,
                checkPhoneNumParams);
    }

    public int checkUserName(String userName) {
        String checkUserNameQuery = "select exists(select userName from Users where userName = ?)";
        String checkUserNameParams = userName;
        return this.jdbcTemplate.queryForObject(checkUserNameQuery,
                int.class,
                checkUserNameParams);
    }

    public int modifyUser(int userIdx, PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update Users set userName = ?, profileImgUrl = ? where userIdx = ?";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getProfileImgUrl(), userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public List<User> getUserByPhoneNum(PostLoginReq postLoginReq) {
        String getUserQuery =
                "select userIdx, phoneNumber, status\n" +
                "from Users\n" +
                "where phoneNumber = ?";
        Object[] getUserParams = new Object[]{postLoginReq.getPhoneNumber()};
        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("phoneNumber"),
                        rs.getString("status")),
                getUserParams);
    }

    public List<GetUserItemRes> getUserItems(int userIdx, int salesStatus) {
        String getUserItemsQuery =
                "select itemIdx, itemName, concat(cost,'원') as cost,\n" +
                "       (select Images.imageUrl from Images where Images.imageIdx = (select min(Images.imageIdx)\n" +
                "                                                                    from Images\n" +
                "                                                                        inner join ItemImages ii on Images.itemImageIdx = ii.itemImageIdx\n" +
                "                                                                        inner join Items i2 on ii.itemIdx = i2.itemIdx\n" +
                "                                                                    where i2.itemIdx = i.itemIdx)) as imageUrl,\n" +
                "       salesStatus, isSafePayment,\n" +
                "       case when timestampdiff(second , i.updatedAt, current_timestamp) <60\n" +
                "        then concat(timestampdiff(second, i.updatedAt, current_timestamp),' 초 전')\n" +
                "        when timestampdiff(minute , i.updatedAt, current_timestamp) <60\n" +
                "        then concat(timestampdiff(minute, i.updatedAt, current_timestamp),' 분 전')\n" +
                "        when timestampdiff(hour , i.updatedAt, current_timestamp) <24\n" +
                "        then concat(timestampdiff(hour, i.updatedAt, current_timestamp),' 시간 전')\n" +
                "        else concat(datediff( current_timestamp, i.updatedAt),' 일 전')\n" +
                "    end                                      as uploadTime,\n" +
                "       itemCnt\n" +
                "from (select userIdx from Users) u\n" +
                "    left join Items i on i.userIdx = u.userIdx\n" +
                "    left join (\n" +
                "        select count(itemIdx) as itemCnt, u.userIdx\n" +
                "        from Users u\n" +
                "            left join Items I on u.userIdx = I.userIdx\n" +
                "        group by u.userIdx\n" +
                ") ic on ic.userIdx = u.userIdx\n" +
                "where u.userIdx = ? and salesStatus = ?;";
        Object[] getUserItemsParams = new Object[]{userIdx, salesStatus};
        return this.jdbcTemplate.query(getUserItemsQuery,
                (rs, rowNum) -> new GetUserItemRes(
                        rs.getInt("itemIdx"),
                        rs.getString("itemName"),
                        rs.getString("imageUrl"),
                        rs.getString("cost"),
                        rs.getInt("isSafePayment"),
                        rs.getString("uploadTime"),
                        rs.getInt("itemCnt")),
                getUserItemsParams);
    }

    public int modifyUserStatus(int userIdx) {
        String modifyUserStatusQuery =
                "update Users set status = 'disable' where userIdx = ? ";
        int modifyUserStatusParams = userIdx;
        return this.jdbcTemplate.update(modifyUserStatusQuery, modifyUserStatusParams);
    }

    public int checkUserStatus(int userIdx) {
        String checkUserStatusQuery = "select exists(select userIdx from Users where userIdx = ? and status = 'enable')\n";
        int checkUserStatusParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserStatusQuery, int.class, checkUserStatusParams);
    }

    public List<String> getUserStatus(PostUserReq postUserReq) {
        String getUserStatusQuery = "select status from Users where userName = ? and phoneNumber = ? and birth = ?";
        Object[] getUserStatusParams = new Object[]{postUserReq.getUserName(), postUserReq.getPhoneNumber(), postUserReq.getBirth()};
        return this.jdbcTemplate.query(getUserStatusQuery,
                (rs, rowNum) -> new String(
                        rs.getString("status")),
                getUserStatusParams);
    }

    public int modifyUserStatusToEnable(User user) {
        String modifyUserStatusToEnableQuery = "update Users set status = 'enable' where userIdx = ?";
        Object[] modifyUserStatusToEnableParams = new Object[]{user.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserStatusToEnableQuery, modifyUserStatusToEnableParams);
    }
}

