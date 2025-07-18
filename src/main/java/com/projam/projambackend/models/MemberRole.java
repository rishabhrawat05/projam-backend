package com.projam.projambackend.models;

import java.util.HashSet;
import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "member_role")
public class MemberRole {

    @Id
    private String memberRoleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;
    
    @Column(name = "role_color")
    private String roleColor;

    @ManyToMany(mappedBy = "memberRole")
    private Set<Tag> tags;

    @OneToMany(mappedBy = "memberRole", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;

    @ManyToMany(mappedBy = "memberRoles")
    private Set<Member> members = new HashSet<>();
    
    @ManyToOne  
    @JoinColumn(name = "project_id")  
    @JsonIgnore
    private Project project;  

    @Column(nullable = false)
    private boolean canCreateTask;

    @Column(nullable = false)
    private boolean canEditTask;

    @Column(nullable = false)
    private boolean canDeleteTask;

    @Column(nullable = false)
    private boolean canAssignTask;

    @Column(nullable = false)
    private boolean canManageMembers;

    @Column(nullable = false)
    private boolean canEditProject;

    @Column(nullable = false)
    private boolean canDeleteProject;

    @Column(nullable = false)
    private boolean canCreateColumn;

    @Column(nullable = false)
    private boolean canDeleteColumn;
    
    @Column(nullable = false)
    private boolean canManageRolesAndPermission;
    
    @Column(nullable = false)
    private boolean canManageGithub;

    public MemberRole() {
        this.memberRoleId = NanoIdUtils.randomNanoId();
    }


    public String getMemberRoleId() {
        return memberRoleId;
    }

    public void setMemberRoleId(String memberRoleId) {
        this.memberRoleId = memberRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Member> getMember() {
        return members;
    }

    public void setMember(Set<Member> members) {
        this.members = members;
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

	public Project getProject() {
		return project;
	}


	public void setProject(Project project) {
		this.project = project;
	}


	public String getRoleColor() {
		return roleColor;
	}

	public void setRoleColor(String roleColor) {
		this.roleColor = roleColor;
	}
	
	public void addMember(Member member) {
		if(member != null) {
			this.members.add(member);
		}
		
	}


	public Set<Member> getMembers() {
		return members;
	}


	public void setMembers(Set<Member> members) {
		this.members = members;
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
