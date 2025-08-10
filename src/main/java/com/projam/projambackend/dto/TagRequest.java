package com.projam.projambackend.dto;

import java.util.List;
import java.util.Set;


public class TagRequest {

	private String title;
	
	private List<String> memberRoleId;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(List<String> memberRoleId) {
		this.memberRoleId = memberRoleId;
	}

	/**
	 * @param title
	 * @param memberRoleId
	 */
	public TagRequest(String title, List<String> memberRoleId) {
		this.title = title;
		this.memberRoleId = memberRoleId;
	}
	
	public TagRequest() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param title
	 */
	public TagRequest(String title) {
		this.title = title;
	}
	
	
	
}
