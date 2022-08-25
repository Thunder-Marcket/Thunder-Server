
package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@AllArgsConstructor
@Service
public class UserProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final JwtService jwtService;

//    public List<GetUserRes> getUsers() throws BaseException{
//        try{
//            List<GetUserRes> getUserRes = userDao.getUsers();
//            return getUserRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserItemRes> getUserItems(int userIdx, int salesStatus) throws BaseException {
        try {
            List<GetUserItemRes> getUserItemResList = userDao.getUserItems(userIdx, salesStatus);
            return getUserItemResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchUserRes getModifiedUser(int userIdx) throws BaseException {
        try {
            PatchUserRes getUserRes = userDao.getModifiedUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhoneNum(String phoneNumber) throws BaseException{
        try{
            return userDao.checkPhoneNum(phoneNumber);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserName(String userName) throws BaseException {
        try{
            return userDao.checkUserName(userName);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public User getUserByPhoneNum(String phoneNumber) throws BaseException {
        try {
            return userDao.getUserByPhoneNum(phoneNumber);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 탈퇴한 회원인지 확인
    public void isDeleteStatus(int userIdx) throws BaseException {
        int result = userDao.checkUserStatus(userIdx);
        if (result == 0) {
            throw new BaseException(DELETED_USER);
        }
    }
}

