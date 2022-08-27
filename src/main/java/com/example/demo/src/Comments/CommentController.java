package com.example.demo.src.Comments;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Comments.model.GetStoreCommentRes;
import com.example.demo.src.Comments.model.GetUserCommentRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentProvider commentProvider;
    private final CommentService commentService;
    private final JwtService jwtService;

    @Autowired
    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService) {
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }


    /**
     *  상점에 쓰여진 리뷰 조회 API
     *  [GET] /comments/store/:userIdx
     * @return BaseResponse<List<GetStoreCommentRes>>
     */
    @ResponseBody
    @GetMapping("/store/{userIdx}")
    public BaseResponse<List<GetStoreCommentRes>> getStoreComment(@PathVariable("userIdx") int userIdx){
        try{
            List<GetStoreCommentRes> getStoreCommentRes = commentProvider.getStoreComment(userIdx);
            return new BaseResponse<>(getStoreCommentRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     *  내가 쓴 리뷰 조회 API
     *  [GET] /comments/user/:userIdx
     * @return BaseResponse<List<GetStoreCommentRes>>
     */
    @ResponseBody
    @GetMapping("/user/{userIdx}")
    public BaseResponse<List<GetUserCommentRes>> getUserComment(@PathVariable("userIdx") int userIdx){
        try{
            if(userIdx != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetUserCommentRes> getUserCommentRes = commentProvider.getUserComment(userIdx);
            return new BaseResponse<>(getUserCommentRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
