package com.projam.projambackend.dto;

import java.time.LocalDateTime;

import com.projam.projambackend.enums.ProjectStatus;

public class ProjectRequest {

	private String projectName;
	
	private Boolean isPrivate;
	
	private LocalDateTime startDate;
	
	private LocalDateTime endDate;
	
	private ProjectStatus projectStatus;

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

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	
	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}
	
	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}
	
}
