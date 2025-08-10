package com.projam.projambackend.dto;

public class UserProfileResponse {

	
	private String username;
	
	private String gmail;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}

	/**
	 * @param username
	 * @param gmail
	 */
	public UserProfileResponse(String username, String gmail) {
		this.username = username;
		this.gmail = gmail;
	}
	
	public UserProfileResponse() {
		// TODO Auto-generated constructor stub
	}
}
