package com.projam.projambackend.models;

import java.time.LocalDate;
import java.util.Set;

import com.projam.projambackend.enums.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "project")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long projectId;
	
	@Column(name = "project_name", nullable = false)
	private String projectName;
	
	@OneToMany
	@JoinColumn(name = "project_id")
	private Set<Task> tasks;
	
	@ManyToMany
	@JoinTable(
		    name = "project_members",
		    joinColumns = @JoinColumn(name = "project_id"),
		    inverseJoinColumns = @JoinColumn(name = "member_id")
		)
	private Set<Member> members;
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;
	
	@Column(name = "is_private")
	private Boolean isPrivate;
	
	@OneToMany(mappedBy = "project")
	private Set<Activity> activities;

	@ManyToOne
	@JoinColumn(name = "workspace_id")
	private Workspace workspace;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "project_status")
	private ProjectStatus projectStatus;
	
	@Lob
	@Column(name = "project_description")
	private String projectDescription;
	
	@ManyToMany
    @JoinTable(
        name = "project_tags",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "github_installation_id")
	private GithubInstallation githubInstallation;

	@Column(name = "linked_repo_name")
	private String linkedRepoName;

	@Column(name = "linked_repo_owner")
	private String linkedRepoOwner;
	
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
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

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
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
	
	public Set<Tag> getTags() {
		return tags;
	}
	
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
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
	
	
	
}
