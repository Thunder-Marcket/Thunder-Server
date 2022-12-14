
package com.example.demo.src.users;



import com.example.demo.config.BaseException;
import com.example.demo.src.users.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        userProvider.checkPhoneNum(postUserReq.getPhoneNumber());
        userProvider.checkUserName(postUserReq.getUserName());

        try {
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt, userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userProvider.getUserByPhoneNum(postLoginReq).get(0);
//        if (user.getStatus().equals("disable")) {
//            if (userDao.modifyUserStatusToEnable(user) == 0) {
//                throw new BaseException(MODIFY_FAIL_USER_STATUS_TO_ENABLE);
//            }
//        }
        int userIdx = user.getUserIdx();
        userProvider.isDeleteStatus(userIdx);
        try {
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchUserRes modifyUser(int userIdx, PatchUserReq patchUserReq) throws BaseException {
        userProvider.isDeleteStatus(userIdx);
        userProvider.checkUserName(patchUserReq.getUserName());
        int result = userDao.modifyUser(userIdx, patchUserReq);
        if(result == 0){
            throw new BaseException(MODIFY_FAIL_USER);
        }
        try{
            PatchUserRes patchUserRes = userProvider.getModifiedUser(userIdx);
            return patchUserRes;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchUserRes modifyUserStatus(int userIdx) throws BaseException {
        userProvider.isDeleteStatus(userIdx);
        int result = userDao.modifyUserStatus(userIdx);
        if (result == 0) {
            throw new BaseException(MODIFY_FAIL_USER_STATUS);
        }
        try {
            PatchUserRes patchUserRes = userProvider.getModifiedUser(userIdx);
            return patchUserRes;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

