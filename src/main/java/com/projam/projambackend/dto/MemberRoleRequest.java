package com.projam.projambackend.dto;

import java.util.Set;

public class MemberRoleRequest {

	private String roleName;
	
	private String memberRoleId;
	
	private Set<MemberResponse> members;
	
	private String roleColor;
	
	private boolean canCreateTask;

	private boolean canEditTask;

	private boolean canDeleteTask;

	private boolean canAssignTask;

	private boolean canManageMembers;

	private boolean canEditProject;

	private boolean canDeleteProject;

	private boolean canCreateColumn;

	private boolean canDeleteColumn;
	
	private boolean canManageRolesAndPermission;
	
	private boolean canManageGithub;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(String memberRoleId) {
		this.memberRoleId = memberRoleId;
	}

	public Set<MemberResponse> getMembers() {
		return members;
	}

	public void setMembers(Set<MemberResponse> members) {
		this.members = members;
	}

	public String getRoleColor() {
		return roleColor;
	}

	public void setRoleColor(String roleColor) {
		this.roleColor = roleColor;
	}

	public boolean isCanCreateTask() {
		return canCreateTask;
	}

	public void setCanCreateTask(boolean canCreateTask) {
		this.canCreateTask = canCreateTask;
	}

	public boolean isCanEditTask() {
		return canEditTask;
	}

	public void setCanEditTask(boolean canEditTask) {
		this.canEditTask = canEditTask;
	}

	public boolean isCanDeleteTask() {
		return canDeleteTask;
	}

	public void setCanDeleteTask(boolean canDeleteTask) {
		this.canDeleteTask = canDeleteTask;
	}

	public boolean isCanAssignTask() {
		return canAssignTask;
	}

	public void setCanAssignTask(boolean canAssignTask) {
		this.canAssignTask = canAssignTask;
	}

	public boolean isCanManageMembers() {
		return canManageMembers;
	}

	public void setCanManageMembers(boolean canManageMembers) {
		this.canManageMembers = canManageMembers;
	}

	public boolean isCanEditProject() {
		return canEditProject;
	}

	public void setCanEditProject(boolean canEditProject) {
		this.canEditProject = canEditProject;
	}

	public boolean isCanDeleteProject() {
		return canDeleteProject;
	}

	public void setCanDeleteProject(boolean canDeleteProject) {
		this.canDeleteProject = canDeleteProject;
	}

	public boolean isCanCreateColumn() {
		return canCreateColumn;
	}

	public void setCanCreateColumn(boolean canCreateColumn) {
		this.canCreateColumn = canCreateColumn;
	}

	public boolean isCanDeleteColumn() {
		return canDeleteColumn;
	}

	public void setCanDeleteColumn(boolean canDeleteColumn) {
		this.canDeleteColumn = canDeleteColumn;
	}

	public boolean isCanManageRolesAndPermission() {
		return canManageRolesAndPermission;
	}

	public void setCanManageRolesAndPermission(boolean canManageRolesAndPermission) {
		this.canManageRolesAndPermission = canManageRolesAndPermission;
	}

	public boolean isCanManageGithub() {
		return canManageGithub;
	}

	public void setCanManageGithub(boolean canManageGithub) {
		this.canManageGithub = canManageGithub;
	}
	
	
	
}
