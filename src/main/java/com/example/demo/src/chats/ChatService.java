package com.example.demo.src.chats;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.chats.model.PostChatReq;
import com.example.demo.src.chats.model.PostChatRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ChatService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChatDao chatDao;
    private final ChatProvider chatProvider;

    @Autowired
    public ChatService(ChatDao chatDao, ChatProvider chatProvider) {
        this.chatDao = chatDao;
        this.chatProvider = chatProvider;
    }

    public PostChatRes createChat(PostChatReq postChatReq) throws BaseException {
        if(chatDao.checkItem(postChatReq.getItemIdx()) == 0) {
            throw new BaseException(POST_CHATS_INVALID_ITEM);
        }

        if(postChatReq.getChatRoomIdx() == 0){
            int chatRoomIdx = chatDao.checkChatRoomByItem(postChatReq.getUserIdx(), postChatReq.getItemIdx());

            if(chatRoomIdx == 0){
                chatRoomIdx = chatDao.createChatRoom(postChatReq.getUserIdx(), postChatReq.getItemIdx());
            }

            postChatReq.setChatRoomIdx(chatRoomIdx);
        }
        else if(chatDao.checkChatRoom(postChatReq.getUserIdx(), postChatReq.getChatRoomIdx()) == 0){
            throw new BaseException(POST_CHATS_INVALID_CHATROOM);
        }


        try{
            PostChatRes postChatRes = chatDao.createChat(postChatReq);
            return postChatRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
