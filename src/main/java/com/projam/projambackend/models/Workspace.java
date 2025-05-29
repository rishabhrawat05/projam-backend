package com.projam.projambackend.models;

import java.util.HashSet;
import java.util.Set;

import com.projam.projambackend.enums.WorkspaceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
	
	@Column(name = "workspace_type")
	@Enumerated(EnumType.STRING)
	private WorkspaceType workspaceType;
	
	@Column(name = "organization_name", nullable = false)
	private String organizationName;
	
	@Column(name = "is_allowed_invites")
	private Boolean isAllowedInvites;
	
	@ManyToMany(mappedBy = "workspaces")
	private Set<User> users = new HashSet<>();

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

	public WorkspaceType getWorkspaceType() {
		return workspaceType;
	}

	public void setWorkspaceType(WorkspaceType workspaceType) {
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
	}
	
	public void removeUser(User user) {
		this.users.remove(user);
	}

	public Boolean getIsAllowedInvites() {
		return isAllowedInvites;
	}

	public void setIsAllowedInvites(Boolean isAllowedInvites) {
		this.isAllowedInvites = isAllowedInvites;
	}
	
	
}
