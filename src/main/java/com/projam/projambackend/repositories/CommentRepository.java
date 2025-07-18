package com.projam.projambackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.CommentResponse;
import com.projam.projambackend.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

	@Query("SELECT new com.projam.projambackend.dto.CommentResponse(cr.commentId, cr.commentDescription, cr.task.taskId, cr.member.memberName) FROM Comment cr WHERE cr.task.taskId = :taskId ORDER BY cr.createdAt DESC")
	List<CommentResponse> findAllByTaskId(@Param("taskId") String taskId);
}
