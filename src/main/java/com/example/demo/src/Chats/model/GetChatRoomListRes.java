package com.example.demo.src.Chats.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatRoomListRes {
    private String storeImageUrl;
    private String storeName;
    private String period;
    private String lastChat;
    private int itemIdx;
    private int chatRoomIdx;
    private int storeUserIdx;
}
