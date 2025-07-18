package com.projam.projambackend.models;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "github_automation")
public class GithubAutomation {

	@Id
	private String edgeId;
	
	@Column(name = "source_event")
	private String sourceEvent;
	
	@Column(name = "target_column")
	private String targetColumn;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "edge_condition")
	private String edgeCondition;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	@JsonIgnore
	private Project project;

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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	

	public String getCondition() {
		return edgeCondition;
	}

	public void setCondition(String edgeCondition) {
		this.edgeCondition = edgeCondition;
	}
	
	public GithubAutomation() {
		this.edgeId = NanoIdUtils.randomNanoId();
	}

	/**
	 * @param edgeId
	 * @param sourceEvent
	 * @param targetColumn
	 * @param color
	 * @param project
	 */
	public GithubAutomation(String sourceEvent, String targetColumn, String color, String edgeCondition, Project project) {
		this.edgeId = NanoIdUtils.randomNanoId();
		this.sourceEvent = sourceEvent;
		this.targetColumn = targetColumn;
		this.color = color;
		this.edgeCondition = edgeCondition;
		this.project = project;
	}
	
	public GithubAutomation(String sourceEvent, String targetColumn, String color, Project project) {
		this.edgeId = NanoIdUtils.randomNanoId();
		this.sourceEvent = sourceEvent;
		this.targetColumn = targetColumn;
		this.color = color;
		this.project = project;
	}
	
	
	
}
