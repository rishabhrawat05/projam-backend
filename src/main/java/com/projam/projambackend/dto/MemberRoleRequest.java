package com.projam.projambackend.dto;

public class MemberRoleRequest {

	private String roleName;
	
	private Long memberRoleId;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(Long memberRoleId) {
		this.memberRoleId = memberRoleId;
	}
	
	
}
