package com.projam.projambackend.dto;

import java.time.LocalDateTime;

public class MemberResponse {

	private String memberName;
	
	private LocalDateTime memberJoinDate;
	
	private String memberGmail;

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

	public MemberResponse(String memberName, String memberGmail, LocalDateTime memberJoinDate) {
		this.memberName = memberName;
		this.memberJoinDate = memberJoinDate;
		this.memberGmail = memberGmail;
	}
	
	public MemberResponse() {
		
	}
	
}
