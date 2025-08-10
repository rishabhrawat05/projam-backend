package com.projam.projambackend.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.ChatMessageResponse;
import com.projam.projambackend.models.ChatMessage;
import com.projam.projambackend.repositories.ChatMessageRepository;

@RestController
@RequestMapping("/projam/chat")
public class ChatMessageHistoryController {

    private ChatMessageRepository chatRepo;
    
    public ChatMessageHistoryController(ChatMessageRepository chatRepo) {
		this.chatRepo = chatRepo;
	}

    @GetMapping("/history")
    public List<ChatMessageResponse> getChatHistory(@RequestParam String projectId, @RequestParam String email, Principal principal) {
        return chatRepo.findChatHistory(projectId, principal.getName(), email);
    }
}

