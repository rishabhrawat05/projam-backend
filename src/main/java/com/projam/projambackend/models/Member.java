package com.projam.projambackend.models;

import java.time.LocalDateTime;
import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {

	@Id
	private String memberId;
	
	@Column(name = "member_name", nullable = false)
	private String memberName;
	
	@Column(name = "member_gmail", nullable = false)
	private String memberGmail;
	
	@ManyToMany(mappedBy = "members")
	private Set<Project> projects;
	
	@OneToMany(mappedBy = "assignee",  cascade = CascadeType.ALL)
	private Set<Task> assignedTasks;

	@OneToMany(mappedBy = "assignedTo",  cascade = CascadeType.ALL)
	private Set<Task> tasksAssignedTo;
	
	@OneToMany(mappedBy = "member",  cascade = CascadeType.ALL)
	private Set<Activity> activities;
	
	@OneToMany(mappedBy = "member",  cascade = CascadeType.ALL)
	private Set<MemberRole> memberRole;
	
	@ManyToOne
	@JoinColumn(name = "workspace_id")
	private Workspace workspace; 
	
	@Column(name = "member_join_date")
	private LocalDateTime memberJoinDate;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
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

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public LocalDateTime getMemberJoinDate() {
		return memberJoinDate;
	}

	public void setMemberJoinDate(LocalDateTime memberJoinDate) {
		this.memberJoinDate = memberJoinDate;
	}

	public String getMemberGmail() {
		return memberGmail;
	}

	public void setMemberGmail(String memberGmail) {
		this.memberGmail = memberGmail;
	}
	
	public void addProject(Project project) {
		this.projects.add(project);
	}
	
	public void removeProject(Project project) {
		this.projects.remove(project);
	}
	
	public void addMemberRole(MemberRole memberRole) {
		this.memberRole.add(memberRole);
	}
	
	public void removeMemberRole(MemberRole memberRole) {
		this.memberRole.remove(memberRole);
	}
	
	public void addAssignedTask(Task task) {
		this.assignedTasks.add(task);
	}
	
	public void addTaskAssignedTo(Task task) {
		this.tasksAssignedTo.add(task);
	}
	
	public Member() {
		this.memberId = NanoIdUtils.randomNanoId();
	}
	
}
