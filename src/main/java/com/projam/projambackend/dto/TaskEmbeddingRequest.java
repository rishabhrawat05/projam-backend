package com.projam.projambackend.dto;

import java.util.List;

public class TaskEmbeddingRequest {
	
    private String taskId;
    private String title;
    private String description;
    private String status;
    private String endDate;
    private String assigneeMemberName;
    private String assignedToMemberName;
    private String priority;
    private List<String> tags;
    
    
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getAssigneeMemberName() {
		return assigneeMemberName;
	}
	public void setAssigneeMemberName(String assigneeMemberName) {
		this.assigneeMemberName = assigneeMemberName;
	}
	public String getAssignedToMemberName() {
		return assignedToMemberName;
	}
	public void setAssignedToMemberName(String assignedToMemberName) {
		this.assignedToMemberName = assignedToMemberName;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
    
    
    
}

