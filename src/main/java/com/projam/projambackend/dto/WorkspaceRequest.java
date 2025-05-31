package com.projam.projambackend.dto;

import java.util.HashSet;
import java.util.Set;
import com.projam.projambackend.models.User;

public class WorkspaceRequest {

	private String workspaceName;

	private String workspaceType;

	private String organizationName;
	
	private String adminGmail;
	
	private Boolean isAllowedInvites;
	
	private String workspaceRole;

	private Set<User> users = new HashSet<>();

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

	public String getAdminGmail() {
		return adminGmail;
	}

	public void setAdmin_gmail(String adminGmail) {
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

	public void setAdminGmail(String adminGmail) {
		this.adminGmail = adminGmail;
	}
	
}
