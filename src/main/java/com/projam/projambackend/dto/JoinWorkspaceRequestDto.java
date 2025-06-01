package com.projam.projambackend.dto;

public class JoinWorkspaceRequestDto {

	private String gmail;
	
	private String workspaceSlug;
	
	private String joinCode;
	
	public String getGmail() {
		return gmail;
	}
	
	public void setGmail(String gmail) {
		this.gmail = gmail;
	}
	
	public String getWorkspaceSlug() {
		return workspaceSlug;
	}
	
	public void setWorkspaceSlug(String workspaceSlug) {
		this.workspaceSlug = workspaceSlug;
	}

	public String getJoinCode() {
		return joinCode;
	}

	public void setJoinCode(String joinCode) {
		this.joinCode = joinCode;
	}
	
	
}
