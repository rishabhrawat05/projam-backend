package com.projam.projambackend.dto;

public class WorkspaceSummaryDto {

	private String workspaceId;

	private String workspaceName;
	
	private String workspaceSlug;

	private Boolean isPrivate;

	private String organizationName;
	
	private String workspaceType;

	public String getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public String getWorkspaceName() {
		return workspaceName;
	}

	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}


	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
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
	
	

	public String getWorkspaceType() {
		return workspaceType;
	}

	public void setWorkspaceType(String workspaceType) {
		this.workspaceType = workspaceType;
	}

	public void setWorkspaceSlug(String workspaceSlug) {
		this.workspaceSlug = workspaceSlug;
	}

	public WorkspaceSummaryDto(String workspaceId, String workspaceName,
			Boolean isPrivate, String organizationName, String workspaceSlug, String workspaceType) {
		this.workspaceId = workspaceId;
		this.workspaceName = workspaceName;
		this.isPrivate = isPrivate;
		this.organizationName = organizationName;
		this.workspaceSlug = workspaceSlug;
		this.workspaceType = workspaceType;
	}
	
	public WorkspaceSummaryDto() {
		
	}

	

	
}
