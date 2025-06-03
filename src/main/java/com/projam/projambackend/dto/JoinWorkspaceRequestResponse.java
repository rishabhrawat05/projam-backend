package com.projam.projambackend.dto;

import java.time.LocalDateTime;


public class JoinWorkspaceRequestResponse {

	
	private Long requestId;
	
	
    private WorkspaceResponse workspace;

    
    private UserResponse user;

    
    private LocalDateTime requestTime;

    
    private String status;


	public Long getRequestId() {
		return requestId;
	}


	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}


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
    
    
    
    
}
