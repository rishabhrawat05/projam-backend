package com.projam.projambackend.models;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long taskId;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Lob
	@Column(name = "description", nullable = false)
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
	private Project project;
	
	@Column(nullable = false)
	private Integer taskNumber;
	
	@ManyToMany
	@JoinTable(
	    name = "task_tags",
	    joinColumns = @JoinColumn(name = "task_id"),
	    inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tag> tags;
	
	@OneToMany(mappedBy = "task")
	private Set<Activity> activities;
	
	@OneToMany(mappedBy = "task")
	private Set<Comment> comments;
	
	@ManyToOne
	@JoinColumn(name = "member_role_id")
	private MemberRole memberRole;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
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

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
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

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
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
	
	
	
}
