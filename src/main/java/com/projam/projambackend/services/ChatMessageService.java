package com.projam.projambackend.services;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.ChatMessageRequest;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.models.ChatMessage;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.repositories.ChatMessageRepository;
import com.projam.projambackend.repositories.ProjectRepository;

@Service
public class ChatMessageService {

	private  final ChatMessageRepository chatMessageRepository;
	
	private final ProjectRepository projectRepository;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	public ChatMessageService(ChatMessageRepository chatMessageRepository, ProjectRepository projectRepository, SimpMessagingTemplate messagingTemplate) {
		this.chatMessageRepository = chatMessageRepository;
		this.projectRepository = projectRepository;
		this.messagingTemplate = messagingTemplate;
	}
	
	public void handleChat(ChatMessageRequest chatMessageRequest) {
		ChatMessage chatMessage = new ChatMessage();
		Project project = projectRepository.findById(chatMessageRequest.getProjectId()).orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		chatMessage.setContent(chatMessageRequest.getContent());
		chatMessage.setProject(project);
		chatMessage.setReceiver(chatMessageRequest.getReceiver());
		chatMessage.setSender(chatMessageRequest.getSender());
		chatMessage.setTimestamp(LocalDateTime.now());
		chatMessageRepository.save(chatMessage);
		
		messagingTemplate.convertAndSendToUser(
	            chatMessage.getReceiver(),
	            "/queue/messages", 
	            chatMessage
	        );
	}
}
