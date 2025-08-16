package com.projam.projambackend.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "workspace")
public class Workspace {
	
	@Id
	private String workspaceId;
	
	@Column(name = "workspace_name", nullable = false)
	private String workspaceName;
	
	@Column(name = "workspace_slug")
	private String workspaceSlug;
	
	@Column(name = "admin_gmail")
	private String adminGmail;
	
	@Column(name = "workspace_type", nullable = false)
	private String workspaceType;
	
	@Column(name = "organization_name", nullable = false)
	private String organizationName;
	
	@Column(name = "is_allowed_invites")
	private Boolean isAllowedInvites;
	
	@Column(name = "workspace_role")
	private String workspaceRole;
	
	@Column(name = "is_private")
	private Boolean isPrivate;
	
	@Column(name = "join_code", unique = true)
	private String joinCode;
	
	@ManyToMany(mappedBy = "workspaces")
	@JsonIgnore
	private List<User> users = new ArrayList<>();
	
	@OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JoinWorkspaceRequest> requests = new ArrayList<>();

	@OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Project> projects = new ArrayList<>();
	
	@OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Member> members = new ArrayList<>();
	
	@Column(name = "member_count")
	private Integer memberCount;
	
	public String getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public String getWorkspaceName() {
		return workspaceName;
	}

	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}

	public String getWorkspaceType() {
		return workspaceType;
	}

	public void setWorkspaceType(String workspaceType) {
		this.workspaceType = workspaceType;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	public String getWorkspaceSlug() {
		return workspaceSlug;
	}

	public void setWorkspaceSlug(String workspaceSlug) {
		this.workspaceSlug = workspaceSlug;
	}
	
	public void addUser(User user) {
		this.users.add(user);
		user.getWorkspaces().add(this);
	}
	
	public void removeUser(User user) {
		this.users.remove(user);
		user.getWorkspaces().remove(this);
	}

	public Boolean getIsAllowedInvites() {
		return isAllowedInvites;
	}

	public void setIsAllowedInvites(Boolean isAllowedInvites) {
		this.isAllowedInvites = isAllowedInvites;
	}

	public String getAdminGmail() {
		return adminGmail;
	}

	public void setAdminGmail(String adminGmail) {
		this.adminGmail = adminGmail;
	}

	public String getWorkspaceRole() {
		return workspaceRole;
	}

	public void setWorkspaceRole(String workspaceRole) {
		this.workspaceRole = workspaceRole;
	}


	public void addRequest(JoinWorkspaceRequest request) {
	    requests.add(request);
	    request.setWorkspace(this);
	}

	public void removeRequest(JoinWorkspaceRequest request) {
	    requests.remove(request);
	    request.setWorkspace(null);
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getJoinCode() {
		return joinCode;
	}

	public void setJoinCode(String joinCode) {
		this.joinCode = joinCode;
	}
	
	public String generateJoinCode() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).toUpperCase();
	}

	
	public void addMember(Member member) {
		members.add(member);
		member.setWorkspace(this);
	}
	
	public void removeMember(Member member) {
		members.remove(member);
		member.setWorkspace(null);
	}
	
	
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<JoinWorkspaceRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<JoinWorkspaceRequest> requests) {
		this.requests = requests;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public Workspace() {
		this.workspaceId = NanoIdUtils.randomNanoId();
	}

	public Integer getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}
	
	
	
}
