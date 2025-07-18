package com.projam.projambackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.CommentRequest;
import com.projam.projambackend.services.CommentService;

@RestController
@RequestMapping("/projam/comment")
public class CommentController {

	private final CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAllCommentByTaskId(@RequestParam String taskId){
		return ResponseEntity.ok(commentService.getAllCommentByTaskId(taskId));
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createNewComment(@RequestBody CommentRequest commentRequest, @RequestParam String projectId){
		return ResponseEntity.ok(commentService.createNewComment(commentRequest, projectId));
	}
}
