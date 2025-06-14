package com.projam.projambackend.dto;

public class MemberRequest {

	private String memberGmail;
	
	private Long workspaceId;
	
	
	public String getMemberGmail() {
		return memberGmail;
	}
	
	public void setMemberGmail(String memberGmail) {
		this.memberGmail = memberGmail;
	}
	
	public Long getWorkspaceId() {
		return workspaceId;
	}
	
	public void setWorkspaceId(Long workspaceId) {
		this.workspaceId = workspaceId;
	}


	
}
