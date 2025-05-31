package com.projam.projambackend.dto;

import java.util.HashSet;
import java.util.Set;

import com.projam.projambackend.models.User;

public class WorkspaceResponse {

	private String workspaceName;

	private String workspaceType;

	private String workspaceSlug;

	private String organizationName;

	private Set<User> users = new HashSet<>();

	private String adminGmail;

	private Boolean isAllowedInvites;

	private String workspaceRole;

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

	public String getAdminGmail() {
		return adminGmail;
	}

	public void setAdminGmail(String adminGmail) {
		this.adminGmail = adminGmail;
	}

	public Boolean getIsAllowedInvites() {
		return isAllowedInvites;
	}

	public void setIsAllowedInvites(Boolean isAllowedInvites) {
		this.isAllowedInvites = isAllowedInvites;
	}

	public String getWorkspaceRole() {
		return workspaceRole;
	}

	public void setWorkspaceRole(String workspaceRole) {
		this.workspaceRole = workspaceRole;
	}
	
	

}
