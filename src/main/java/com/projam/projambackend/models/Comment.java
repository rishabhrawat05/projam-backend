package com.projam.projambackend.models;

import java.time.LocalDateTime;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment")
public class Comment {
	
	@Id
	private String commentId;
	
	@Lob
	@Column(name = "comment_description", nullable = false, columnDefinition = "TEXT")
	private String commentDescription;
	
	@ManyToOne
	@JoinColumn(name = "task_task_id", nullable = false)
	@JsonIgnore
	private Task task;
	
	@ManyToOne
	private Member member;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommentDescription() {
		return commentDescription;
	}

	public void setCommentDescription(String commentDescription) {
		this.commentDescription = commentDescription;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Comment() {
		this.commentId = NanoIdUtils.randomNanoId();
	}
	
}
