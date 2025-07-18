package com.projam.projambackend.dto;


public class GithubAutomationResponse {

	private String edgeId;
	
	private String sourceEvent;
	
	private String targetColumn;

	private String color;

	private String edgeCondition;
	
	public GithubAutomationResponse() {
		// TODO Auto-generated constructor stub
	}

	public String getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(String edgeId) {
		this.edgeId = edgeId;
	}

	public String getSourceEvent() {
		return sourceEvent;
	}

	public void setSourceEvent(String sourceEvent) {
		this.sourceEvent = sourceEvent;
	}

	public String getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getEdgeCondition() {
		return edgeCondition;
	}

	public void setEdgeCondition(String edgeCondition) {
		this.edgeCondition = edgeCondition;
	}

	/**
	 * @param edgeId
	 * @param sourceEvent
	 * @param targetColumn
	 * @param color
	 * @param edgeCondition
	 */
	public GithubAutomationResponse(String edgeId, String sourceEvent, String targetColumn, String color,
			String edgeCondition) {
		this.edgeId = edgeId;
		this.sourceEvent = sourceEvent;
		this.targetColumn = targetColumn;
		this.color = color;
		this.edgeCondition = edgeCondition;
	}
	
	
	
	
}
