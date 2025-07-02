package com.projam.projambackend.dto;

import java.time.LocalDateTime;


public class ActivityResponse {

	private String activityId;

	private String description;

	private LocalDateTime timeStamp;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @param activityId
	 * @param description
	 * @param timeStamp
	 */
	public ActivityResponse(String activityId, String description, LocalDateTime timeStamp) {
		this.activityId = activityId;
		this.description = description;
		this.timeStamp = timeStamp;
	}
	
	
	
}
