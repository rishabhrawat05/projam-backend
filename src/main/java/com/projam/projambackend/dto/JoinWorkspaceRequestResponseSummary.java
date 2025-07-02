package com.projam.projambackend.dto;

import java.time.LocalDateTime;

public class JoinWorkspaceRequestResponseSummary {

	private String requestId;
	
	private String workspaceId;
	
	private String userId;
	
	private String username;
	
	private String gmail;
	
	private LocalDateTime requestTime;
	
	private String status;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
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
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param requestId
	 * @param workspaceId
	 * @param userId
	 * @param gmail
	 * @param requestTime
	 */
	public JoinWorkspaceRequestResponseSummary(String requestId, String workspaceId, String userId, String gmail,
			LocalDateTime requestTime, String status, String username) {
		this.requestId = requestId;
		this.workspaceId = workspaceId;
		this.userId = userId;
		this.gmail = gmail;
		this.requestTime = requestTime;
		this.status = status;
		this.username = username;
	}
	
	public JoinWorkspaceRequestResponseSummary() {
		
	}
}
