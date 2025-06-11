package com.projam.projambackend.dto;

import java.time.LocalDate;

import com.projam.projambackend.enums.ProjectStatus;

public class ProjectRequest {

	private String projectName;
	
	private Boolean isPrivate;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private ProjectStatus projectStatus;
	
	private String projectDescription;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}
	
	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	
	
	
}
