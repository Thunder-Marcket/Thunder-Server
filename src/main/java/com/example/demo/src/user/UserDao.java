
package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@AllArgsConstructor
@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from UserInfo";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public PatchUserRes getUser(int userIdx){
        String getUserQuery =
                "select userIdx, userName, profileImgUrl\n" +
                "from Users\n" +
                "where userIdx = ?;";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new PatchUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("profileImgUrl")),
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

    public int modifyUserName(int userIdx, PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update Users set userName = ?, profileImgUrl = ? where userIdx = ?";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getProfileImgUrl(), userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

//    public User getPwd(PostLoginReq postLoginReq){
//        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
//        String getPwdParams = postLoginReq.getId();
//
//        return this.jdbcTemplate.queryForObject(getPwdQuery,
//                (rs,rowNum)-> new User(
//                        rs.getInt("userIdx"),
//                        rs.getString("ID"),
//                        rs.getString("userName"),
//                        rs.getString("password"),
//                        rs.getString("email")
//                ),
//                getPwdParams
//                );
//
//    }

    public User getUser(PostLoginReq postLoginReq) {
        String getUserQuery =
                "select userIdx, phoneNumber\n" +
                "from Users\n" +
                "where phoneNumber = ?";
        Object[] getUserParams = new Object[]{postLoginReq.getPhoneNumber()};
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("phoneNumber")),
                getUserParams);
    }
}

