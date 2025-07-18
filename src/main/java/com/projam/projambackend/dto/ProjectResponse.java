package com.projam.projambackend.dto;

import java.time.LocalDate;
import java.util.Date;

import com.projam.projambackend.enums.ProjectStatus;

public class ProjectResponse {

	private String projectId;

	private String projectName;

	private Boolean isPrivate;

	private LocalDate startDate;

	private LocalDate endDate;
	
	private Date deletionMarkedAt;

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

	public ProjectResponse() {

	}
	

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	

	public Date getDeletionMarkedAt() {
		return deletionMarkedAt;
	}

	public void setDeletionMarkedAt(Date deletionMarkedAt) {
		this.deletionMarkedAt = deletionMarkedAt;
	}

	public ProjectResponse(String projectId, String projectName, Boolean isPrivate, LocalDate startDate, LocalDate endDate,
			ProjectStatus projectStatus, Date deletionMarkedAt) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.isPrivate = isPrivate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.projectStatus = projectStatus;
		this.deletionMarkedAt = deletionMarkedAt;
	}

}
