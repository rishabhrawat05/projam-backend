package com.projam.projambackend.dto;

import java.util.ArrayList;
import java.util.List;


public class WorkspaceResponse {
	
	private String workspaceId;

	private String workspaceName;

	private String workspaceType;

	private String workspaceSlug;

	private String organizationName;

	private List<UserResponse> users = new ArrayList<>();

	private String adminGmail;

	private Boolean isAllowedInvites;

	private String workspaceRole;
	
	private Boolean isPrivate;
	
	private String joinCode;
	
	private List<JoinWorkspaceRequestResponse> requests = new ArrayList<>();
	
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

	public String getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public List<UserResponse> getUsers() {
		return users;
	}

	public void setUsers(List<UserResponse> users) {
		this.users = users;
	}

	public List<JoinWorkspaceRequestResponse> getRequests() {
		return requests;
	}

	public void setRequests(List<JoinWorkspaceRequestResponse> requests) {
		this.requests = requests;
	}
	
	

}
