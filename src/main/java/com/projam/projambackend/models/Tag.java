package com.projam.projambackend.models;

import java.util.List;
import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {

	@Id
	private String tagId;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@ManyToMany
	@JoinTable(
	    name = "tag_member_role",
	    joinColumns = @JoinColumn(name = "tag_id"),
	    inverseJoinColumns = @JoinColumn(name = "member_role_id")
	)
	private List<MemberRole> memberRole;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
    private Project project;

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public List<MemberRole> getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(List<MemberRole> memberRole) {
		this.memberRole = memberRole;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Tag() {
		this.tagId = NanoIdUtils.randomNanoId();
	}
}
