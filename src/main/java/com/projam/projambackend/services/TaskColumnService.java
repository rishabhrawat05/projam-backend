package com.projam.projambackend.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.dto.TagRequest;
import com.projam.projambackend.dto.TaskColumnRequest;
import com.projam.projambackend.dto.TaskColumnResponse;
import com.projam.projambackend.dto.TaskResponse;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.TaskColumnAlreadyExistException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.Tag;
import com.projam.projambackend.models.Task;
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
		Optional<TaskColumn> taskColumn = taskColumnRepository.findByTaskColumnSlugAndProject_ProjectId(taskColumnSlug, taskColumnRequest.getProjectId());
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
	
	public List<TaskColumnResponse> getAllTaskColumnByProjectId(Long projectId) {
	    List<TaskColumn> columns = taskColumnRepository.findAllByProject_ProjectId(projectId);
	    return columns.stream()
	        .map(this::mapToTaskColumnResponse)
	        .collect(Collectors.toList());
	}

	
	public TaskColumnResponse mapToTaskColumnResponse(TaskColumn column) {
	    TaskColumnResponse dto = new TaskColumnResponse();
	    dto.setTaskColumnName(column.getTaskColumnName());
	    dto.setTaskColumnColor(column.getTaskColumnColor());
	    dto.setProjectId(column.getProject().getProjectId());
	    dto.setWorkspaceId(column.getWorkspace().getWorkspaceId());

	    Set<TaskResponse> taskResponses = column.getTasks().stream()
	        .map(this::mapToTaskResponse)
	        .collect(Collectors.toSet());

	    dto.setTasks(taskResponses);
	    return dto;
	}
	
	public TaskResponse mapToTaskResponse(Task task) {
	    TaskResponse dto = new TaskResponse();
	    dto.setTaskId(task.getTaskId());
	    dto.setTitle(task.getTitle());
	    dto.setDescription(task.getDescription());
	    dto.setStartDate(task.getStartDate());
	    dto.setEndDate(task.getEndDate());
	    dto.setStatus(task.getStatus());
	    dto.setTaskNumber(task.getTaskNumber());
	    dto.setTaskKey(task.getTaskKey());
	    dto.setGithubIssueLink(task.getGithubIssueLink());
	    dto.setGithubRepoName(task.getGithubRepoName());
	    dto.setGithubStatus(task.getGithubStatus());
	    dto.setIsIntegrated(task.getIsIntegrated());
	    dto.setGithubPullRequestLink(task.getGithubPullRequestLink());
	    if (task.getAssignee() != null)
	        dto.setAssignee(mapToMemberResponse(task.getAssignee()));

	    if (task.getAssignedTo() != null)
	        dto.setAssignedTo(mapToMemberResponse(task.getAssignedTo()));

	    if(task.getTags() != null) {
	    Set<TagRequest> tags = task.getTags().stream()
	        .map(this::mapToTagRequest)
	        .collect(Collectors.toSet());
	    dto.setTags(tags);
	    }
	    else dto.setTags(null);
	    return dto;
	}
	
	public MemberResponse mapToMemberResponse(Member member) {
	    return new MemberResponse(
	        member.getMemberName(),
	        member.getMemberGmail(),
	        member.getMemberJoinDate()
	    );
	}
	
	public TagRequest mapToTagRequest(Tag tag) {
	    TagRequest tagDto = new TagRequest();
	    tagDto.setTitle(tag.getTitle());
	    
	    Set<Long> memberRoleIds = tag.getMemberRole().stream()
	        .map(role -> role.getMemberRoleId()) 
	        .collect(Collectors.toSet());

	    tagDto.setMemberRoleId(memberRoleIds);
	    return tagDto;
	}
}
