package com.projam.projambackend.dto;

public class TagResponse {

	private String tagId;
	
	private String title;

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param tagId
	 * @param title
	 */
	public TagResponse(String tagId, String title) {
		this.tagId = tagId;
		this.title = title;
	}
	
	
}
