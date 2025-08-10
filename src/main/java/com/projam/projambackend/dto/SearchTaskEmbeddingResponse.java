package com.projam.projambackend.dto;

import java.util.List;

public class SearchTaskEmbeddingResponse {
	private String taskId;
    private Double score;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}

	
	
	
}
