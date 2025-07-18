package com.projam.projambackend.dto;

public class CommentResponse {

	private String commentId;
	
	private String commentDescription;
	
	private String taskId;
	
	private String userName;
	

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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param commentDescription
	 * @param taskId
	 * @param userName
	 */
	public CommentResponse(String commentId, String commentDescription, String taskId, String userName) {
		this.commentId = commentId;
		this.commentDescription = commentDescription;
		this.taskId = taskId;
		this.userName = userName;
	}
	
	
	
}
