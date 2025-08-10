package com.projam.projambackend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskResponse {

	private String taskId;

	private String title;

	private String description;

	private LocalDate startDate;

	private LocalDate endDate;

	private MemberResponse assignee;

	private MemberResponse assignedTo;

	private String status;

	private Integer taskNumber;

	private String taskKey;

	private String githubIssueLink;

	private String githubRepoName;

	private String githubIssueStatus;

	private String githubPrStatus;

	private Boolean isIntegrated;

	private String githubPullRequestLink;

	private List<TagRequest> tags;

	private Integer priority;

	private LocalDateTime createdAt;

	private String taskColumnId;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Integer taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getGithubIssueLink() {
		return githubIssueLink;
	}

	public void setGithubIssueLink(String githubIssueLink) {
		this.githubIssueLink = githubIssueLink;
	}

	public String getGithubRepoName() {
		return githubRepoName;
	}

	public void setGithubRepoName(String githubRepoName) {
		this.githubRepoName = githubRepoName;
	}

	public String getGithubIssueStatus() {
		return githubIssueStatus;
	}

	public void setGithubIssueStatus(String githubIssueStatus) {
		this.githubIssueStatus = githubIssueStatus;
	}

	public String getGithubPrStatus() {
		return githubPrStatus;
	}

	public void setGithubPrStatus(String githubPrStatus) {
		this.githubPrStatus = githubPrStatus;
	}

	public Boolean getIsIntegrated() {
		return isIntegrated;
	}

	public void setIsIntegrated(Boolean isIntegrated) {
		this.isIntegrated = isIntegrated;
	}

	public String getGithubPullRequestLink() {
		return githubPullRequestLink;
	}

	public void setGithubPullRequestLink(String githubPullRequestLink) {
		this.githubPullRequestLink = githubPullRequestLink;
	}

	public List<TagRequest> getTags() {
		return tags;
	}

	public void setTags(List<TagRequest> tags) {
		this.tags = tags;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getTaskColumnId() {
		return taskColumnId;
	}

	public void setTaskColumnId(String taskColumnId) {
		this.taskColumnId = taskColumnId;
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
	 * @param taskNumber
	 * @param taskKey
	 * @param githubIssueLink
	 * @param githubRepoName
	 * @param githubIssueStatus
	 * @param githubPrStatus
	 * @param isIntegrated
	 * @param githubPullRequestLink
	 * @param tags
	 * @param priority
	 * @param createdAt
	 * @param taskColumnId
	 */
	public TaskResponse(String taskId, String title, String description, LocalDate startDate, LocalDate endDate,
			MemberResponse assignee, MemberResponse assignedTo, String status, Integer taskNumber, String taskKey,
			String githubIssueLink, String githubRepoName, String githubIssueStatus, String githubPrStatus,
			Boolean isIntegrated, String githubPullRequestLink, List<TagRequest> tags, Integer priority,
			LocalDateTime createdAt, String taskColumnId) {
		this.taskId = taskId;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.assignee = assignee;
		this.assignedTo = assignedTo;
		this.status = status;
		this.taskNumber = taskNumber;
		this.taskKey = taskKey;
		this.githubIssueLink = githubIssueLink;
		this.githubRepoName = githubRepoName;
		this.githubIssueStatus = githubIssueStatus;
		this.githubPrStatus = githubPrStatus;
		this.isIntegrated = isIntegrated;
		this.githubPullRequestLink = githubPullRequestLink;
		this.tags = tags;
		this.priority = priority;
		this.createdAt = createdAt;
		this.taskColumnId = taskColumnId;
	}

	public TaskResponse() {

	}

	public TaskResponse(String title, String taskId, String taskColumnId, String taskKey, LocalDate endDate, Integer priority,
			List<String> tagTitles) {
		this.title = title;
		this.taskId = taskId;
		this.taskColumnId = taskColumnId;
		this.taskKey = taskKey;
		this.endDate = endDate;
		this.priority = priority;
		this.tags = tagTitles.stream().map(TagRequest::new).collect(Collectors.toList());
	}

}
