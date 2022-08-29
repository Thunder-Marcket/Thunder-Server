package com.example.demo.src.Comments;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Comments.model.PostCommentReq;
import com.example.demo.src.Comments.model.PostCommentRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CommentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentDao commentDao;
    private final CommentProvider commentProvider;

    @Autowired
    public CommentService(CommentDao commentDao, CommentProvider commentProvider) {
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
    }

//    public PostCommentRes createComment(PostCommentReq postCommentReq) throws BaseException {
//        try{
//            PostCommentRes postCommentRes = commentDao.createComment(postCommentReq);
//            return postCommentRes;
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }
}
