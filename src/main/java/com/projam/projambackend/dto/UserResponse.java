package com.projam.projambackend.dto;

import java.util.HashSet;
import java.util.Set;

import com.projam.projambackend.models.Role;


public class UserResponse {

	private String userId;
	
	
	private String username;
	
	
	private String gmail;
	
	
	private Set<WorkspaceResponse> workspaces = new HashSet<>();
	
	
	private Set<Role> roles;
	
	
	private boolean isVerified = false;

	
	private Set<JoinWorkspaceRequestResponse> joinRequests = new HashSet<>();


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}



	public String getGmail() {
		return gmail;
	}


	public void setGmail(String gmail) {
		this.gmail = gmail;
	}



	public Set<WorkspaceResponse> getWorkspaces() {
		return workspaces;
	}


	public void setWorkspaces(Set<WorkspaceResponse> workspaces) {
		this.workspaces = workspaces;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}



	public boolean isVerified() {
		return isVerified;
	}


	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}


	public Set<JoinWorkspaceRequestResponse> getJoinRequests() {
		return joinRequests;
	}


	public void setJoinRequests(Set<JoinWorkspaceRequestResponse> joinRequests) {
		this.joinRequests = joinRequests;
	}
	
	
}
