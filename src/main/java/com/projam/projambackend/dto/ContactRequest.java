package com.projam.projambackend.dto;

public class ContactRequest {

	private String name;
	
	private String type;
	
	private String message;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
