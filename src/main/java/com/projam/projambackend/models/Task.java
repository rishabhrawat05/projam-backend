package com.projam.projambackend.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "task")
public class Task {

	@Id
	private String taskId;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Lob
	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;
	
	@ManyToOne
	@JoinColumn(name = "assignee_id")
	private Member assignee;
	
	@ManyToOne
	@JoinColumn(name = "assigned_to_id")
	private Member assignedTo;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	@JsonIgnore
	private Project project;
	
	@Column(nullable = false)
	private Integer taskNumber;
	
	@Column(name = "task_key", unique = true)
    private String taskKey;
	
	@Column(name = "assigned_at")
	private LocalDateTime assignedAt;
	
	@Column(name = "completed_at")
	private LocalDateTime completedAt;
	
	@Column(name = "priority")
	private Integer priority;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	
	@ManyToMany
	@JoinTable(
	    name = "task_tags",
	    joinColumns = @JoinColumn(name = "task_id"),
	    inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private List<Tag> tags = new ArrayList<>();
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Activity> activities;
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<Comment>();
	
	@ManyToOne
	@JoinColumn(name = "member_role_id")
	private MemberRole memberRole;
	
	@ManyToOne
	@JoinColumn(name = "task_column_id", nullable = true)
	private TaskColumn taskColumn;
	
	@Column(name = "github_issue_link")
	private String githubIssueLink;
	
	@Column(name = "github_issue_status")
	private String githubIssueStatus;

	@Column(name = "github_pr_status")
	private String githubPrStatus;
	
	@Column(name = "github_repo_name")
	private String githubRepoName;
	
	@Column(name = "is_integrated")
	private Boolean isIntegrated;
	
	@Column(name = "github_pull_request_link")
	private String githubPullRequestLink;

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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	public MemberRole getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(MemberRole memberRole) {
		this.memberRole = memberRole;
	}

	public Integer getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Integer taskNumber) {
		this.taskNumber = taskNumber;
	}

	public TaskColumn getTaskColumn() {
		return taskColumn;
	}

	public void setTaskColumn(TaskColumn taskColumn) {
		this.taskColumn = taskColumn;
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

	public String getGithubRepoName() {
		return githubRepoName;
	}

	public void setGithubRepoName(String githubRepoName) {
		this.githubRepoName = githubRepoName;
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
	

	public LocalDateTime getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(LocalDateTime assignedAt) {
		this.assignedAt = assignedAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}
	
	public Task() {
		this.taskId = NanoIdUtils.randomNanoId();
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

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
