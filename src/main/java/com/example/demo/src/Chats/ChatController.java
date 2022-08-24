package com.example.demo.src.Chats;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
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
}
