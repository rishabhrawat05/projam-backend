package com.projam.projambackend.models;

import java.time.LocalDateTime;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "join_project_request")
public class JoinProjectRequest {

	@Id
	private String requestId;
	
	@ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Column(name = "status")
    private String status;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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

    public JoinProjectRequest() {
		this.requestId = NanoIdUtils.randomNanoId();
	}
}
