package com.example.demo.src.chats.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostChatReq {
    private int chatRoomIdx;
    private int itemIdx;
    private int userIdx;
    private String message;
}
