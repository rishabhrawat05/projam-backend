package com.projam.projambackend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.CommentRequest;
import com.projam.projambackend.dto.CommentResponse;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.TaskNotFoundException;
import com.projam.projambackend.models.Comment;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.Task;
import com.projam.projambackend.repositories.CommentRepository;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskRepository;

@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	private final TaskRepository taskRepository;
	
	private final MemberRepository memberRepository;
	
	
	
	public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, MemberRepository memberRepository) {
		this.commentRepository = commentRepository;
		this.taskRepository = taskRepository;
		this.memberRepository = memberRepository;
	}

	public String createNewComment(CommentRequest commentRequest, String projectId) {
		Comment comment = new Comment();
		comment.setCommentDescription(commentRequest.getCommentDescription());
		Task task = taskRepository.findById(commentRequest.getTaskId()).orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
		comment.setTask(task);
		Member member = memberRepository.findByMemberGmailAndProjectId(commentRequest.getUserGmail(), projectId).orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
		comment.setMember(member);
		comment.setCreatedAt(LocalDateTime.now());
		commentRepository.save(comment);
		return "New Comment Created Successfully";
		
	}
	
	public List<CommentResponse> getAllCommentByTaskId(String taskId){
		return commentRepository.findAllByTaskId(taskId);
		
	}
}
