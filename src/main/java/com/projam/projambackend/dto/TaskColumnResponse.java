package com.projam.projambackend.dto;

public class TaskColumnResponse {
	
	private String taskColumnId;

	private String taskColumnName;

	private String taskColumnColor;

	private String workspaceId;

	private String projectId;
	
	private int taskColumnIndex;
	

	public String getTaskColumnId() {
		return taskColumnId;
	}

	public void setTaskColumnId(String taskColumnId) {
		this.taskColumnId = taskColumnId;
	}

	public String getTaskColumnName() {
		return taskColumnName;
	}

	public void setTaskColumnName(String taskColumnName) {
		this.taskColumnName = taskColumnName;
	}

	public String getTaskColumnColor() {
		return taskColumnColor;
	}

	public void setTaskColumnColor(String taskColumnColor) {
		this.taskColumnColor = taskColumnColor;
	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public int getTaskColumnIndex() {
		return taskColumnIndex;
	}

	public void setTaskColumnIndex(int taskColumnIndex) {
		this.taskColumnIndex = taskColumnIndex;
	}
	
	public TaskColumnResponse() {
		
	}

	
}
