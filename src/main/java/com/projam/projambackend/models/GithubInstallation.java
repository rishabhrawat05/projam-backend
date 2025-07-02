package com.projam.projambackend.models;

import java.time.Instant;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "github_installation")
public class GithubInstallation {

    @Id
    private String id;

    @Column(name = "installation_id", nullable = false, unique = true)
    private Long installationId;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "github_username", nullable = false)
    private String githubUsername;

    @Column(name = "connected_at")
    private Instant connectedAt;
    
    @ManyToOne
    @JoinColumn(name = "workspaceId")
    private Workspace workspace;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getInstallationId() {
		return installationId;
	}

	public void setInstallationId(Long installationId) {
		this.installationId = installationId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getGithubUsername() {
		return githubUsername;
	}

	public void setGithubUsername(String githubUsername) {
		this.githubUsername = githubUsername;
	}

	public Instant getConnectedAt() {
		return connectedAt;
	}

	public void setConnectedAt(Instant connectedAt) {
		this.connectedAt = connectedAt;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}
	
	public GithubInstallation() {
		this.id = NanoIdUtils.randomNanoId();
	}

	
    
}

