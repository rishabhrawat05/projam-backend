package com.projam.projambackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projam.projambackend.dto.ChatMessageResponse;
import com.projam.projambackend.models.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    @Query("SELECT new com.projam.projambackend.dto.ChatMessageResponse(cm.chatId, cm.sender, cm.receiver, cm.content, cm.timestamp) FROM ChatMessage cm WHERE (cm.sender = :user1 AND cm.receiver = :user2) OR (cm.sender = :user2 AND cm.receiver = :user1) AND cm.project.projectId = :projectId ORDER BY cm.timestamp ASC")
    List<ChatMessageResponse> findChatHistory(@Param("projectId") String projectId, @Param("user1") String user1, @Param("user2") String user2);
}

