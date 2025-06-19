package com.projam.projambackend.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_column")
public class TaskColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long taskColumnId;
	
	@Column(name = "task_column_name", nullable = false)
	private String taskColumnName;
	
	@Column(name = "task_col_color", nullable = false)
	private String taskColumnColor;
	
	@Column(name = "task_col_slug")
	private String taskColumnSlug;
	
	@OneToMany
	private Set<Task> tasks;
	
	@ManyToOne
	@JoinColumn(name = "workspace_id")
	private Workspace workspace;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	public Long getTaskColumnId() {
		return taskColumnId;
	}

	public void setTaskColumnId(Long taskColumnId) {
		this.taskColumnId = taskColumnId;
	}

	public String getTaskColumnName() {
		return taskColumnName;
	}

	public void setTaskColumnName(String taskColumnName) {
		this.taskColumnName = taskColumnName;
	}

	public String getTaskColumnColor() {
		return taskColumnColor;
	}

	public void setTaskColumnColor(String taskColumnColor) {
		this.taskColumnColor = taskColumnColor;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getTaskColumnSlug() {
		return taskColumnSlug;
	}

	public void setTaskColumnSlug(String taskColumnSlug) {
		this.taskColumnSlug = taskColumnSlug;
	}
	
	
	
}
