package com.projam.projambackend.dto;

import java.util.HashSet;
import java.util.Set;

import com.projam.projambackend.enums.WorkspaceType;
import com.projam.projambackend.models.User;

public class WorkspaceResponse {

	private String workspaceName;

	private WorkspaceType workspaceType;

	private String workspaceSlug;
	
	private String organizationName;

	private Set<User> users = new HashSet<>();

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
	
	
}
