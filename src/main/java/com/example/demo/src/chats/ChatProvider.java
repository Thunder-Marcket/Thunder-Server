package com.example.demo.src.chats;

import com.example.demo.config.BaseException;
import com.example.demo.src.chats.model.GetChatRoomListRes;
import com.example.demo.src.chats.model.GetChatRoomRes;
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

    public List<GetChatRoomRes> getChatRoom(int chatRoomIdx) throws BaseException {
        try{
            List<GetChatRoomRes> getChatRoomRes = chatDao.getChatRoom(chatRoomIdx);
            return getChatRoomRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkChatRoom(int userIdx, int chatRoomIdx) throws BaseException{
        try{
            return chatDao.checkChatRoom(userIdx, chatRoomIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
