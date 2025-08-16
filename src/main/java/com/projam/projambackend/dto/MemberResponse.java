package com.projam.projambackend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MemberResponse {
	
	private String memberId;

	private String memberName;
	
	private LocalDateTime memberJoinDate;
	
	private String memberGmail;
	
	private List<String> roleName; 
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public LocalDateTime getMemberJoinDate() {
		return memberJoinDate;
	}

	public void setMemberJoinDate(LocalDateTime memberJoinDate) {
		this.memberJoinDate = memberJoinDate;
	}

	public String getMemberGmail() {
		return memberGmail;
	}

	public void setMemberGmail(String memberGmail) {
		this.memberGmail = memberGmail;
	}
	
	

	public List<String> getRoleName() {
		return roleName;
	}

	public void setRoleName(List<String> roleName) {
		this.roleName = roleName;
	}

	public MemberResponse(String memberId, String memberName, String memberGmail, LocalDateTime memberJoinDate, List<String> roleName) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.memberJoinDate = memberJoinDate;
		this.memberGmail = memberGmail;
		this.roleName = roleName;
	}
	
	public MemberResponse(String memberId, String memberName, String memberGmail, LocalDateTime memberJoinDate) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.memberJoinDate = memberJoinDate;
		this.memberGmail = memberGmail;
	}
	
	public MemberResponse() {
		
	}
	
}
