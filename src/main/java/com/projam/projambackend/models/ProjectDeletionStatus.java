package com.projam.projambackend.models;

import java.time.LocalDateTime;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.projam.projambackend.enums.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_deletion_status")
public class ProjectDeletionStatus {
	
    @Id
    private String projectDeletionStatusId;

    @Column(name = "project_id")
    private String projectId;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;


	public String getProjectDeletionStatusId() {
		return projectDeletionStatusId;
	}

	public void setProjectDeletionStatusId(String projectDeletionStatusId) {
		this.projectDeletionStatusId = projectDeletionStatusId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

    public ProjectDeletionStatus() {
		this.projectDeletionStatusId = NanoIdUtils.randomNanoId();
	}
}

