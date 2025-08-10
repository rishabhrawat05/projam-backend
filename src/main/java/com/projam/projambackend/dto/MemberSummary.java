package com.projam.projambackend.dto;

public class MemberSummary {

	private String memberName;
	
	private String memberGmail;

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberGmail() {
		return memberGmail;
	}

	public void setMemberGmail(String memberGmail) {
		this.memberGmail = memberGmail;
	}

	/**
	 * @param memberName
	 * @param memberGmail
	 */
	public MemberSummary(String memberName, String memberGmail) {
		this.memberName = memberName;
		this.memberGmail = memberGmail;
	}
	
	
	
}
