package com.projam.projambackend.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;
	
	@Column(name = "role_name", nullable = false)
	private String roleName;

	public Long getRoleId() {
		return roleId;
	}
	
	@ManyToMany(mappedBy = "roles")
	private Set<User> users;

	public void setRoleId(Long roleId) {
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
	}
	
	public Role() {
		
	}
	
	
}
