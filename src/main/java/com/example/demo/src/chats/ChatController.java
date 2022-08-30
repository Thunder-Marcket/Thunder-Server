package com.example.demo.src.chats;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.chats.model.GetChatRoomListRes;
import com.example.demo.src.chats.model.GetChatRoomRes;
import com.example.demo.src.chats.model.PostChatReq;
import com.example.demo.src.chats.model.PostChatRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/chat-rooms")
public class ChatController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ChatProvider chatProvier;

    @Autowired
    private final ChatService chatService;

    @Autowired
    private final JwtService jwtService;


    public ChatController(ChatProvider chatProvier, ChatService chatService, JwtService jwtService) {
        this.chatProvier = chatProvier;
        this.chatService = chatService;
        this.jwtService = jwtService;
    }


    /**
     * 내가 대화한 번개톡방 리스트 가져오는 API
     * [GET] /chats
     * @return BaseResponse<List<GetChatRoomListRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetChatRoomListRes>> getChatRoomList(){
        try{
            int userIdx = jwtService.getUserIdx();

            List<GetChatRoomListRes> getChatRoomListRes = chatProvier.getChatRoomList(userIdx);
            return new BaseResponse<>(getChatRoomListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 특정 채팅방의 채팅 내역들 가져오는 API
     * [GET] /chat-rooms/:roomIdx
     * @return BaseResponse<List<GetChatRoom>>
     */
    @ResponseBody
    @GetMapping("/{roomIdx}")
    public BaseResponse<List<GetChatRoomRes>> getChatRoom(@PathVariable("roomIdx") int chatRoomIdx){
        try{
            if(chatProvier.checkChatRoom(jwtService.getUserIdx(), chatRoomIdx) == 0){
                return new BaseResponse<>(INVALID_USER_JWT);
            }


            List<GetChatRoomRes> getChatRoomRes = chatProvier.getChatRoom(chatRoomIdx);
            return new BaseResponse<>(getChatRoomRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 채팅방에 메시지 보내는 API
     * [POST] /chat-rooms
     * @return BaseResponse<PostChatRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostChatRes> createChat(@RequestBody PostChatReq postChatReq){
        try{
            if(postChatReq.getUserIdx() != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(postChatReq.getMessage() == null || postChatReq.getMessage().length() < 1){
                return new BaseResponse<>(POST_CHATS_EMPTY_CHAT);
            }


            PostChatRes postChatRes = chatService.createChat(postChatReq);
            return new BaseResponse<>(postChatRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
