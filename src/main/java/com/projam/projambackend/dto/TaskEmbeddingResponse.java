package com.projam.projambackend.dto;

import java.util.List;

public class TaskEmbeddingResponse {
	
    private String taskId;
    private List<Double> embedding;
    
    
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public List<Double> getEmbedding() {
		return embedding;
	}
	public void setEmbedding(List<Double> embedding) {
		this.embedding = embedding;
	}

    
}

