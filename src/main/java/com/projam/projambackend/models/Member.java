package com.projam.projambackend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.projam.projambackend.enums.MemberPlan;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
	private List<Project> projects = new ArrayList<>();
	
	@OneToMany(mappedBy = "assignee",  cascade = CascadeType.ALL)
	private List<Task> assignedTasks;

	@OneToMany(mappedBy = "assignedTo",  cascade = CascadeType.ALL)
	private List<Task> tasksAssignedTo;
	
	@OneToMany(mappedBy = "member",  cascade = CascadeType.ALL)
	private List<Activity> activities;
	
	@ManyToMany
	@JoinTable(
	    name = "member_member_role",
	    joinColumns = @JoinColumn(name = "member_id"),
	    inverseJoinColumns = @JoinColumn(name = "member_role_id")
	)
	private Set<MemberRole> memberRoles = new HashSet<>();
	
	@Column(name = "request_status")
	private String requestStatus;
	
	@ManyToOne
	@JoinColumn(name = "workspace_id")
	private Workspace workspace; 
	
	@Column(name = "member_join_date")
	private LocalDateTime memberJoinDate;
	
	@Column(name = "plan")
	@Enumerated(EnumType.STRING)
	private MemberPlan plan;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public List<Task> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(List<Task> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	public List<Task> getTasksAssignedTo() {
		return tasksAssignedTo;
	}

	public void setTasksAssignedTo(List<Task> tasksAssignedTo) {
		this.tasksAssignedTo = tasksAssignedTo;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
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
		this.memberRoles.add(memberRole);
	}
	
	public void removeMemberRole(MemberRole memberRole) {
		this.memberRoles.remove(memberRole);
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

	public Set<MemberRole> getMemberRoles() {
		return memberRoles;
	}

	public void setMemberRoles(Set<MemberRole> memberRoles) {
		this.memberRoles = memberRoles;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public MemberPlan getPlan() {
		return plan;
	}

	public void setPlan(MemberPlan plan) {
		this.plan = plan;
	}
	
	
}
