package com.example.demo.src.Chats;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Chats.model.GetChatRoomListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ChatProvider {

    private final ChatDao chatDao;

    @Autowired
    public ChatProvider(ChatDao chatDao) {
        this.chatDao = chatDao;
    }


    public List<GetChatRoomListRes> getChatRoomList(int userIdx) throws BaseException {
        try{
            List<GetChatRoomListRes> getChatRoomListRes = chatDao.getChatRoomList(userIdx);
            return getChatRoomListRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
