package com.projam.projambackend.models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User implements UserDetails{

	@Id
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "gmail", nullable = false)
	private String gmail;
	
	@Column(name = "refresh_token")
	private String refreshToken;
	
	@Column(name = "otp")
	private String otp;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "user_workspaces",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "workspace_id"))
	@JsonIgnore
	private Set<Workspace> workspaces = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> roles = new HashSet<>();
	
	@Column(name = "otp_generated_time", nullable = false)
	private LocalDateTime otpGeneratedTime;
	
	@Column(name = "is_verified", nullable = false)
	private boolean isVerified = false;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<JoinWorkspaceRequest> joinRequests = new HashSet<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName())).collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.gmail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getOtpGeneratedTime() {
		return otpGeneratedTime;
	}

	public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
		this.otpGeneratedTime = otpGeneratedTime;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Set<Workspace> getWorkspaces() {
		return workspaces;
	}

	public void setWorkspaces(Set<Workspace> workspaces) {
		this.workspaces = workspaces;
	}
	
	public void addWorkspace(Workspace workspace) {
		this.workspaces.add(workspace);
		workspace.getUsers().add(this);
	}
	
	public void deleteWorkspace(Workspace workspace) {
		if(this.workspaces.contains(workspace)) {
			this.workspaces.remove(workspace);
			workspace.getUsers().remove(this);
		}
	}

	public Set<JoinWorkspaceRequest> getJoinRequests() {
		return joinRequests;
	}

	public void setJoinRequests(Set<JoinWorkspaceRequest> joinRequests) {
		this.joinRequests = joinRequests;
	}
	
	public void addRequest(JoinWorkspaceRequest request) {
	    joinRequests.add(request);
	    request.setUser(this);
	}

	public void removeRequest(JoinWorkspaceRequest request) {
	    joinRequests.remove(request);
	    request.setUser(null);
	}
	
	public User() {
		this.userId = NanoIdUtils.randomNanoId();
	}
	
	public String getUserName() {
		return this.username;
	}
}
