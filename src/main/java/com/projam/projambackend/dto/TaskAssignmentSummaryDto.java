package com.projam.projambackend.dto;

public class TaskAssignmentSummaryDto {

	private String taskId;
	
	private String title;
	
	private String status;
	
	private String taskKey;
	
	private String columnColor;
	

	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	

	public String getTaskKey() {
		return taskKey;
	}



	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	

	public String getColumnColor() {
		return columnColor;
	}



	public void setColumnColor(String columnColor) {
		this.columnColor = columnColor;
	}



	/**
	 * @param taskId
	 * @param title
	 * @param status
	 */
	public TaskAssignmentSummaryDto(String taskId, String title, String status, String taskKey, String columnColor) {
		this.taskId = taskId;
		this.title = title;
		this.status = status;
		this.taskKey = taskKey;
		this.columnColor = columnColor;
	}
	
	
}
