package com.projam.projambackend.models;

import java.time.LocalDateTime;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "join_workspace_token")
public class JoinWorkspaceToken {

	@Id
	private String tokenId;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "email")
	private String email;
	
	@ManyToOne
	private Workspace workspace;
	
	@Column(name = "token_expiration_time")
	private LocalDateTime expiresAt;
	
	@Column(name = "token_used")
	private Boolean used;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}
	
	
	public JoinWorkspaceToken() {
		this.tokenId = NanoIdUtils.randomNanoId();
	}
	
}
