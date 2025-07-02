package com.projam.projambackend.models;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment")
public class Comment {
	
	@Id
	private String commentId;
	
	@Column(name = "comment_description", nullable = false)
	private String commentDescription;
	
	@ManyToOne
	private Task task;
	
	@Column(name = "user_gmail", nullable = false)
	private String userGmail;

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

	public String getUserGmail() {
		return userGmail;
	}

	public void setUserGmail(String userGmail) {
		this.userGmail = userGmail;
	}
	
	public Comment() {
		this.commentId = NanoIdUtils.randomNanoId();
	}
	
}
