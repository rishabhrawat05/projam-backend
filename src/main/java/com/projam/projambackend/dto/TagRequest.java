package com.projam.projambackend.dto;

import java.util.Set;


public class TagRequest {

	private String title;
	
	private Set<Long> memberRoleId;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Long> getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(Set<Long> memberRoleId) {
		this.memberRoleId = memberRoleId;
	}
	
	
}
