package com.projam.projambackend.dto;

public class EditProjectResponse {

	private String projectName;
	
	private String projectDescription;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/**
	 * @param projectName
	 * @param projectDescription
	 */
	public EditProjectResponse(String projectName, String projectDescription) {
		this.projectName = projectName;
		this.projectDescription = projectDescription;
	}
	
	
}
