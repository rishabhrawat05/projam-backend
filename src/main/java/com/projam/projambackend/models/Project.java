package com.projam.projambackend.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.projam.projambackend.enums.ProjectStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "project")
public class Project {

	@Id
	private String projectId;
	
	@Column(name = "project_name", nullable = false)
	private String projectName;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<Task>();
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
		    name = "project_members",
		    joinColumns = @JoinColumn(name = "project_id"),
		    inverseJoinColumns = @JoinColumn(name = "member_id")
		)
	private List<Member> members = new ArrayList<>();
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;
	
	@Column(name = "is_private")
	private Boolean isPrivate;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Activity> activities = new ArrayList<Activity>();

	@ManyToOne
	@JoinColumn(name = "workspace_id")
	private Workspace workspace;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "project_status", length = 50)
	private ProjectStatus projectStatus;
	
	@Lob
	@Column(name = "project_description")
	private String projectDescription;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deletion_marked_at")
	private Date deletionMarkedAt;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<Tag>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "github_installation_id")
	private GithubInstallation githubInstallation;

	@Column(name = "linked_repo_name")
	private String linkedRepoName;

	@Column(name = "linked_repo_owner")
	private String linkedRepoOwner;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<MemberRole> memberRoles = new ArrayList<MemberRole>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GithubAutomation> edges = new ArrayList<>();
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskColumn> taskColumns = new ArrayList<TaskColumn>();
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JoinProjectRequest> joinRequests = new ArrayList<>();
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JoinProjectToken> joinToken = new ArrayList<>();
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<WeeklyProgress> weeklyProgress = new ArrayList<>();
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatMessage> chatMessage = new ArrayList<>();
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}


	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public ProjectStatus getStatus() {
		return projectStatus;
	}

	public void setStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	
	public void addMember(Member member) {
		this.members.add(member);
	}
	
	public void removeMember(Member member) {
		this.members.remove(member);
	}
	
	

	public GithubInstallation getGithubInstallation() {
		return githubInstallation;
	}

	public void setGithubInstallation(GithubInstallation githubInstallation) {
		this.githubInstallation = githubInstallation;
	}

	public String getLinkedRepoName() {
		return linkedRepoName;
	}

	public void setLinkedRepoName(String linkedRepoName) {
		this.linkedRepoName = linkedRepoName;
	}

	public String getLinkedRepoOwner() {
		return linkedRepoOwner;
	}

	public void setLinkedRepoOwner(String linkedRepoOwner) {
		this.linkedRepoOwner = linkedRepoOwner;
	}
	

	public List<GithubAutomation> getEdges() {
		return edges;
	}

	public void setEdges(List<GithubAutomation> edges) {
		this.edges = edges;
	}
	
	public Date getDeletionMarkedAt() {
		return deletionMarkedAt;
	}

	public void setDeletionMarkedAt(Date deletionMarkedAt) {
		this.deletionMarkedAt = deletionMarkedAt;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<MemberRole> getMemberRoles() {
		return memberRoles;
	}

	public void setMemberRoles(List<MemberRole> memberRoles) {
		this.memberRoles = memberRoles;
	}

	public List<TaskColumn> getTaskColumns() {
		return taskColumns;
	}

	public void setTaskColumns(List<TaskColumn> taskColumns) {
		this.taskColumns = taskColumns;
	}

	public List<JoinProjectRequest> getJoinRequests() {
		return joinRequests;
	}

	public void setJoinRequests(List<JoinProjectRequest> joinRequests) {
		this.joinRequests = joinRequests;
	}

	public List<JoinProjectToken> getJoinToken() {
		return joinToken;
	}

	public void setJoinToken(List<JoinProjectToken> joinToken) {
		this.joinToken = joinToken;
	}

	public List<WeeklyProgress> getWeeklyProgress() {
		return weeklyProgress;
	}

	public void setWeeklyProgress(List<WeeklyProgress> weeklyProgress) {
		this.weeklyProgress = weeklyProgress;
	}

	public List<ChatMessage> getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(List<ChatMessage> chatMessage) {
		this.chatMessage = chatMessage;
	}

	public Project() {
		this.projectId = NanoIdUtils.randomNanoId();
	}
	
	
}
