package com.example.demo.src.Comments;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Comments.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class CommentProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommentDao commentDao;

    @Autowired
    public CommentProvider(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public GetStoreCommentListRes getStoreComment(int userIdx) throws BaseException {
        try{
            GetStoreCommentListRes getStoreCommentRes = commentDao.getStoreComment(userIdx);
            return getStoreCommentRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserCommentListRes getUserComment(int userIdx) throws BaseException {
        try{
            GetUserCommentListRes getUserCommentRes = commentDao.getUserComment(userIdx);
            return getUserCommentRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public int canCreateComment(PostCommentReq postCommentReq) throws BaseException {
//        try{
//            if(commentDao.existOrder(postCommentReq) == 0){
//                throw new BaseException(POST_COMMENTS_UNACCESS_ORDER);
//            }
//
//
//
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//
//    }
}
