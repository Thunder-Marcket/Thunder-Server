package com.example.demo.src.chats;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chats.model.GetChatRoomListRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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



}
