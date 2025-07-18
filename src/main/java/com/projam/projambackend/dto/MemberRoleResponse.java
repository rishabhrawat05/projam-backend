package com.projam.projambackend.dto;

public class MemberRoleResponse {

	private String memberRoleId;

	private String roleName;
	
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

	public String getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(String memberRoleId) {
		this.memberRoleId = memberRoleId;
	}

	public String getMemberRoleName() {
		return roleName;
	}

	public void setMemberRoleName(String memberRoleName) {
		this.roleName = memberRoleName;
	}

	public MemberRoleResponse() {

	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	
	

	public String getRoleColor() {
		return roleColor;
	}

	public void setRoleColor(String roleColor) {
		this.roleColor = roleColor;
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

	/**
	 * @param memberRoleId
	 * @param roleName
	 * @param roleColor
	 * @param canCreateTask
	 * @param canEditTask
	 * @param canDeleteTask
	 * @param canAssignTask
	 * @param canManageMembers
	 * @param canEditProject
	 * @param canDeleteProject
	 * @param canCreateColumn
	 * @param canDeleteColumn
	 * @param canManageRolesAndPermission
	 * @param canManageGithub
	 */
	public MemberRoleResponse(String memberRoleId, String roleName, String roleColor, boolean canCreateTask,
			boolean canEditTask, boolean canDeleteTask, boolean canAssignTask, boolean canManageMembers,
			boolean canEditProject, boolean canDeleteProject, boolean canCreateColumn, boolean canDeleteColumn,
			boolean canManageRolesAndPermission, boolean canManageGithub) {
		this.memberRoleId = memberRoleId;
		this.roleName = roleName;
		this.roleColor = roleColor;
		this.canCreateTask = canCreateTask;
		this.canEditTask = canEditTask;
		this.canDeleteTask = canDeleteTask;
		this.canAssignTask = canAssignTask;
		this.canManageMembers = canManageMembers;
		this.canEditProject = canEditProject;
		this.canDeleteProject = canDeleteProject;
		this.canCreateColumn = canCreateColumn;
		this.canDeleteColumn = canDeleteColumn;
		this.canManageRolesAndPermission = canManageRolesAndPermission;
		this.canManageGithub = canManageGithub;
	}

	

	
}
