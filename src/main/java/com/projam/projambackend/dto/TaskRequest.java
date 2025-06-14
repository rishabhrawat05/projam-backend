package com.projam.projambackend.dto;

import java.time.LocalDate;
import java.util.Set;

import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.Tag;

public class TaskRequest {

	private String title;
	
	private String description;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private Member assignee;
	
	private Member assignedTo;
	
	private String status;
	
	private Set<Tag> tags;

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

	public Member getAssignee() {
		return assignee;
	}

	public void setAssignee(Member assignee) {
		this.assignee = assignee;
	}

	public Member getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Member assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	
}
