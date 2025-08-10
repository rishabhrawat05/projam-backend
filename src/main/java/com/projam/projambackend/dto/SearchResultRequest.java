package com.projam.projambackend.dto;

import java.util.List;

public class SearchResultRequest {

	private List<Double> queryEmbedding;

	private List<TaskEmbeddingResponse> taskEmbeddings;

	private List<TaskResponse> tasks; 
	
	private List<String> taskIds;	
	
	private String assignedToEmail;

	private String queryText;

	public List<Double> getQueryEmbedding() {
		return queryEmbedding;
	}

	public void setQueryEmbedding(List<Double> queryEmbedding) {
		this.queryEmbedding = queryEmbedding;
	}

	public List<TaskEmbeddingResponse> getTaskEmbeddings() {
		return taskEmbeddings;
	}

	public void setTaskEmbeddings(List<TaskEmbeddingResponse> taskEmbeddings) {
		this.taskEmbeddings = taskEmbeddings;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public List<TaskResponse> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskResponse> tasks) {
		this.tasks = tasks;
	}

	public List<String> getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(List<String> taskIds) {
		this.taskIds = taskIds;
	}

	public String getAssignedToEmail() {
		return assignedToEmail;
	}

	public void setAssignedToEmail(String assignedToEmail) {
		this.assignedToEmail = assignedToEmail;
	}
	
	

}
