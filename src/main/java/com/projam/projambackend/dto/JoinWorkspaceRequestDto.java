package com.projam.projambackend.dto;

public class JoinWorkspaceRequestDto {

	private String gmail;
	
	private String workspaceSlug;
	
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
}
