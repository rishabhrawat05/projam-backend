package com.projam.projambackend.dto;

import java.util.Set;

public class TaskColumnResponse {

	private String taskColumnName;

	private String taskColumnColor;

	private Long workspaceId;

	private Long projectId;
	
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

	public Long getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(Long workspaceId) {
		this.workspaceId = workspaceId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Set<TaskResponse> getTasks() {
		return tasks;
	}

	public void setTasks(Set<TaskResponse> tasks) {
		this.tasks = tasks;
	}

	
	
}
