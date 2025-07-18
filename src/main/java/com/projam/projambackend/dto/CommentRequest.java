package com.projam.projambackend.dto;

public class CommentRequest {

	private String commentDescription;
	
	private String taskId;
	
	private String userGmail;

	public String getCommentDescription() {
		return commentDescription;
	}

	public void setCommentDescription(String commentDescription) {
		this.commentDescription = commentDescription;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserGmail() {
		return userGmail;
	}

	public void setUserGmail(String userGmail) {
		this.userGmail = userGmail;
	}
	
	
}
