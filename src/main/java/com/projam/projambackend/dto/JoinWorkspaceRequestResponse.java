package com.projam.projambackend.dto;

import java.time.LocalDateTime;


public class JoinWorkspaceRequestResponse {

	
    private WorkspaceResponse workspace;

    
    private UserResponse user;

    
    private LocalDateTime requestTime;

    
    private String status;



	public WorkspaceResponse getWorkspace() {
		return workspace;
	}


	public void setWorkspace(WorkspaceResponse workspace) {
		this.workspace = workspace;
	}


	public UserResponse getUser() {
		return user;
	}


	public void setUser(UserResponse user) {
		this.user = user;
	}


	public LocalDateTime getRequestTime() {
		return requestTime;
	}


	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	
	public JoinWorkspaceRequestResponse() {
		
	}
    
    
    
    
}
