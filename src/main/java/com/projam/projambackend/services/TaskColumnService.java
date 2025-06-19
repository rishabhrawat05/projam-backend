package com.projam.projambackend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.TaskColumnRequest;
import com.projam.projambackend.dto.TaskColumnResponse;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.TaskColumnAlreadyExistException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.TaskColumn;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskColumnRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

@Service
public class TaskColumnService {

	private final TaskColumnRepository taskColumnRepository;
	
	private final ProjectRepository projectRepository;
	
	private final WorkspaceRepository workspaceRepository;
	
	public TaskColumnService(TaskColumnRepository taskColumnRepository, ProjectRepository projectRepository, WorkspaceRepository workspaceRepository) {
		this.taskColumnRepository = taskColumnRepository;
		this.projectRepository = projectRepository;
		this.workspaceRepository = workspaceRepository;
	}
	
	public TaskColumnResponse addColumn(TaskColumnRequest taskColumnRequest) {
		String taskColumnSlug = taskColumnRequest.getTaskColumnName().toLowerCase().replaceAll(" ", "");
		Optional<TaskColumn> taskColumn = taskColumnRepository.findByTaskColumnSlug(taskColumnSlug);
		if(taskColumn.isPresent()) {
			throw new TaskColumnAlreadyExistException("Task Column with name " + taskColumnRequest.getTaskColumnName() + " already exists");
		}
		TaskColumn newTaskColumn = new TaskColumn();
		newTaskColumn.setTaskColumnColor(taskColumnRequest.getTaskColumnColor());
		newTaskColumn.setTaskColumnName(taskColumnRequest.getTaskColumnName());
		newTaskColumn.setTaskColumnSlug(taskColumnSlug);
		newTaskColumn.setProject(projectRepository.findById(taskColumnRequest.getProjectId()).orElseThrow(() -> new ProjectNotFoundException("Project Not Found")));
		newTaskColumn.setWorkspace(workspaceRepository.findById(taskColumnRequest.getWorkspaceId()).orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found")));
		newTaskColumn.setTasks(null);
		taskColumnRepository.save(newTaskColumn);
		return taskColumnToTaskColumnResponse(newTaskColumn);
		
	}
	
	public TaskColumnResponse taskColumnToTaskColumnResponse(TaskColumn taskColumn) {
		TaskColumnResponse taskResponse = new TaskColumnResponse();
		taskResponse.setProjectId(taskColumn.getProject().getProjectId());
		taskResponse.setWorkspaceId(taskColumn.getWorkspace().getWorkspaceId());
		taskResponse.setTaskColumnName(taskColumn.getTaskColumnName());
		taskResponse.setTaskColumnColor(taskColumn.getTaskColumnColor());
		return taskResponse;
	}
}
