package com.example.demo.src.Comments;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Comments.model.PostCommentReq;
import com.example.demo.src.Comments.model.PostCommentRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;


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

    public PostCommentRes createComment(PostCommentReq postCommentReq, int orderIdx) throws BaseException {
        if(commentProvider.canCreateComment(postCommentReq, orderIdx) == 0){
            throw new BaseException(POST_COMMENTS_UNABLE_WRITE);
        }



        try{
            PostCommentRes postCommentRes = commentDao.createComment(postCommentReq, orderIdx);
            return postCommentRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
