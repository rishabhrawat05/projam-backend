package com.projam.projambackend.dto;

import java.util.Set;


public class TagRequest {

	private String title;
	
	private Set<String> memberRoleId;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public Set<String> getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(Set<String> memberRoleId) {
		this.memberRoleId = memberRoleId;
	}
	
	
}
