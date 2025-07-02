package com.projam.projambackend.models;

import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

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

    @ManyToMany(mappedBy = "memberRole")
    private Set<Tag> tags;

    @OneToMany(mappedBy = "memberRole", fetch = FetchType.LAZY)
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    @ManyToOne  
    @JoinColumn(name = "project_id")  
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
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
    
    
}
