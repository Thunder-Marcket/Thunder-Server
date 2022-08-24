
package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
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
        if (userProvider.checkPhoneNum(postUserReq.getPhoneNumber()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_PHONENUM);
        }
        if (userProvider.checkUserName(postUserReq.getUserName()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NAME);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchUserRes modifyUserName(int userIdx, PatchUserReq patchUserReq) throws BaseException {
        if (userProvider.checkUserName(patchUserReq.getUserName()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NAME);
        }
        int result = userDao.modifyUserName(userIdx, patchUserReq);
        if(result == 0){
            throw new BaseException(MODIFY_FAIL_USER);
        }
        try{
            PatchUserRes patchUserRes = userProvider.getUser(userIdx);
            return patchUserRes;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

