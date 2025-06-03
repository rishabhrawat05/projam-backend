package com.projam.projambackend.dto;

public class WorkspaceSummaryDto {

	private Long workspaceId;

	private String workspaceName;
	
	private String workspaceSlug;

	private Boolean isPrivate;

	private String organizationName;

	public Long getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(Long workspaceId) {
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

	public void setWorkspaceSlug(String workspaceSlug) {
		this.workspaceSlug = workspaceSlug;
	}

	public WorkspaceSummaryDto(Long workspaceId, String workspaceName,
			Boolean isPrivate, String organizationName, String workspaceSlug) {
		this.workspaceId = workspaceId;
		this.workspaceName = workspaceName;
		this.isPrivate = isPrivate;
		this.organizationName = organizationName;
		this.workspaceSlug = workspaceSlug;
	}
	
	public WorkspaceSummaryDto() {
		
	}

	

	
}
