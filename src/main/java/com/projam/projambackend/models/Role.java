package com.projam.projambackend.models;

import java.util.List;
import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

	@Id
	private String roleId;
	
	@Column(name = "role_name", nullable = false)
	private String roleName;

	public String getRoleId() {
		return roleId;
	}
	
	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public Role(String roleName) {
		this.roleName = roleName;
		this.roleId = NanoIdUtils.randomNanoId();
	}
	
	public Role() {
		this.roleId = NanoIdUtils.randomNanoId();
	}
	
	
}
