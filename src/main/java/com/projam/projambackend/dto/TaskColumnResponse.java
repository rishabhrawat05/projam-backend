package com.projam.projambackend.dto;

import java.util.Set;

public class TaskColumnResponse {

	private String taskColumnName;

	private String taskColumnColor;

	private String workspaceId;

	private String projectId;
	
	private int taskColumnIndex;
	
	private Set<TaskResponse> tasks;

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

	public Set<TaskResponse> getTasks() {
		return tasks;
	}

	public void setTasks(Set<TaskResponse> tasks) {
		this.tasks = tasks;
	}

	public int getTaskColumnIndex() {
		return taskColumnIndex;
	}

	public void setTaskColumnIndex(int taskColumnIndex) {
		this.taskColumnIndex = taskColumnIndex;
	}

	
	
}
