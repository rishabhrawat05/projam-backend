package com.projam.projambackend.controllers;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.projam.projambackend.dto.ChatMessageRequest;
import com.projam.projambackend.services.ChatMessageService;

@Controller
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    
    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat")
    public void handleChat(@Payload ChatMessageRequest message, Principal principal) {
       chatMessageService.handleChat(message, principal.getName());
    }
}

