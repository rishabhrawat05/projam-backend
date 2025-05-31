package com.projam.projambackend.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long workspaceId;
	
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
	
	@ManyToMany(mappedBy = "workspaces")
	private Set<User> users = new HashSet<>();
	
	@OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<JoinWorkspaceRequest> requests = new HashSet<>();

	public Long getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(Long workspaceId) {
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
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

	public Set<JoinWorkspaceRequest> getRequests() {
		return requests;
	}

	public void setRequests(Set<JoinWorkspaceRequest> requests) {
		this.requests = requests;
	}
	
	public void addRequest(JoinWorkspaceRequest request) {
	    requests.add(request);
	    request.setWorkspace(this);
	}

	public void removeRequest(JoinWorkspaceRequest request) {
	    requests.remove(request);
	    request.setWorkspace(null);
	}
}
