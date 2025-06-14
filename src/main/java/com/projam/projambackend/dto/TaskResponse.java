package com.projam.projambackend.dto;

import java.time.LocalDate;
import java.util.Set;

import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.Tag;

public class TaskResponse {
	
	private Long taskId;

	private String title;

	private String description;

	private LocalDate startDate;

	private LocalDate endDate;

	private MemberResponse assignee;

	private MemberResponse assignedTo;

	private String status;
	
	private Set<TagResponse> tags;

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

	public MemberResponse getAssignee() {
		return assignee;
	}

	public void setAssignee(MemberResponse assignee) {
		this.assignee = assignee;
	}

	public MemberResponse getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(MemberResponse assignedTo) {
		this.assignedTo = assignedTo;
	}

	public void setTags(Set<TagResponse> tags) {
		this.tags = tags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	/**
	 * @param taskId
	 * @param title
	 * @param description
	 * @param startDate
	 * @param endDate
	 * @param assignee
	 * @param assignedTo
	 * @param status
	 * @param tags
	 */
	public TaskResponse(Long taskId, String title, String description, LocalDate startDate, LocalDate endDate,
			MemberResponse assignee, MemberResponse assignedTo, String status, Set<TagResponse> tags) {
		this.taskId = taskId;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.assignee = assignee;
		this.assignedTo = assignedTo;
		this.status = status;
		this.tags = tags;
	}
	
	public TaskResponse() {
		
	}
	
}
