package com.projam.projambackend.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberRoleId;
	
	@Column(name = "role_name", nullable = false)
	private String roleName;
	
	@OneToMany(mappedBy = "memberRole", fetch = FetchType.EAGER)
	private Set<Permission> permissions;
	
	@ManyToMany(mappedBy = "memberRole")
	private Set<Tag> tags;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public Long getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(Long memberRoleId) {
		this.memberRoleId = memberRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
	
}
