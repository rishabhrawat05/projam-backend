package com.projam.projambackend.models;

import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
	private Set<MemberRole> memberRole;
	
	@ManyToMany(mappedBy = "tags")
    private Set<Project> projects;

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

	public Set<MemberRole> getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(Set<MemberRole> memberRole) {
		this.memberRole = memberRole;
	}
	
	public Set<Project> getProjects() {
		return projects;
	}
	
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	
	public Tag() {
		this.tagId = NanoIdUtils.randomNanoId();
	}
}
