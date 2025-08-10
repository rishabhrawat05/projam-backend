package com.projam.projambackend.models;

import java.util.List;
import java.util.Set;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_column")
public class TaskColumn {

	@Id
	private String taskColumnId;
	
	@Column(name = "task_column_name", nullable = false)
	private String taskColumnName;
	
	@Column(name = "task_col_color", nullable = false)
	private String taskColumnColor;
	
	@Column(name = "task_col_slug")
	private String taskColumnSlug;
	
	@OneToMany(mappedBy = "taskColumn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Task> tasks;
	
	@ManyToOne
	@JoinColumn(name = "workspace_id")
	private Workspace workspace;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;
	
	@Column(name = "task_column_index")
	private int taskColumnIndex;

	public String getTaskColumnId() {
		return taskColumnId;
	}

	public void setTaskColumnId(String taskColumnId) {
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

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
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
	
	

	public int getTaskColumnIndex() {
		return taskColumnIndex;
	}

	public void setTaskColumnIndex(int taskColumnIndex) {
		this.taskColumnIndex = taskColumnIndex;
	}

	/**
	 * @param taskColumnId
	 * @param taskColumnName
	 * @param taskColumnColor
	 * @param taskColumnSlug
	 * @param tasks
	 * @param workspace
	 * @param project
	 */
	public TaskColumn(String taskColumnName, String taskColumnColor, String taskColumnSlug,
			Workspace workspace, Project project, int taskColumnIndex) {
		this.taskColumnId = NanoIdUtils.randomNanoId();
		this.taskColumnName = taskColumnName;
		this.taskColumnColor = taskColumnColor;
		this.taskColumnSlug = taskColumnSlug;
		this.workspace = workspace;
		this.project = project;
		this.taskColumnIndex = taskColumnIndex;
	}
	
	public TaskColumn() {
		this.taskColumnId = NanoIdUtils.randomNanoId();
	}
	
	
}
