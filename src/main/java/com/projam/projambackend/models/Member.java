package com.projam.projambackend.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;
	
	@Column(name = "member_name", nullable = false)
	private String memberName;
	
	@ManyToMany(mappedBy = "members")
	private Set<Project> projects;
	
	@OneToMany(mappedBy = "assignee")
	private Set<Task> assignedTasks;

	@OneToMany(mappedBy = "assignedTo")
	private Set<Task> tasksAssignedTo;
	
	@OneToMany(mappedBy = "member")
	private Set<Activity> activities;
	
	@OneToMany(mappedBy = "member")
	private Set<MemberRole> memberRole;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Set<Task> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(Set<Task> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	public Set<Task> getTasksAssignedTo() {
		return tasksAssignedTo;
	}

	public void setTasksAssignedTo(Set<Task> tasksAssignedTo) {
		this.tasksAssignedTo = tasksAssignedTo;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Set<MemberRole> getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(Set<MemberRole> memberRole) {
		this.memberRole = memberRole;
	}
	
	
	
}
