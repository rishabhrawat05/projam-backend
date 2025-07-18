package com.projam.projambackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projam.projambackend.models.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    @Query("SELECT m FROM ChatMessage m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) AND m.project.projectId = :projectId ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(@Param("projectId") String projectId, @Param("user1") String user1, @Param("user2") String user2);
}

