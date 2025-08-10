package com.projam.projambackend.dto;

import java.time.LocalDateTime;

public class ChatMessageResponse {

	private String chatId;

    private String sender;
	
    private String receiver;
    
    private String content;

    private LocalDateTime timestamp;

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param chatId
	 * @param sender
	 * @param receiver
	 * @param content
	 * @param timestamp
	 */
	public ChatMessageResponse(String chatId, String sender, String receiver, String content, LocalDateTime timestamp) {
		this.chatId = chatId;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
	}
    
    
}
