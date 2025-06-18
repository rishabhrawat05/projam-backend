package com.projam.projambackend.dto;

import java.time.LocalDate;
import java.util.Set;

public class TaskRequest {

	private String title;
	
	private String description;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private MemberRequest assignee;
	
	private MemberRequest assignedTo;
	
	private String status;
	
	private Set<TagRequest> tags;

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

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public MemberRequest getAssignee() {
		return assignee;
	}

	public void setAssignee(MemberRequest assignee) {
		this.assignee = assignee;
	}

	public MemberRequest getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(MemberRequest assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<TagRequest> getTags() {
		return tags;
	}

	public void setTags(Set<TagRequest> tags) {
		this.tags = tags;
	}
	
	
}
