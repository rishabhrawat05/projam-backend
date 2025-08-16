package com.projam.projambackend.services;

import java.util.Comparator;
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
import com.projam.projambackend.exceptions.MemberNotAuthorizedException;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.TaskColumnAlreadyExistException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.Tag;
import com.projam.projambackend.models.Task;
import com.projam.projambackend.models.TaskColumn;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskColumnRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

@Service
public class TaskColumnService {

	private final TaskColumnRepository taskColumnRepository;
	
	private final ProjectRepository projectRepository;
	
	private final WorkspaceRepository workspaceRepository;
	
	private final MemberRepository memberRepository;
	
	public TaskColumnService(TaskColumnRepository taskColumnRepository, ProjectRepository projectRepository, WorkspaceRepository workspaceRepository, MemberRepository memberRepository) {
		this.taskColumnRepository = taskColumnRepository;
		this.projectRepository = projectRepository;
		this.workspaceRepository = workspaceRepository;
		this.memberRepository = memberRepository;
	}
	
	public TaskColumnResponse addColumn(TaskColumnRequest taskColumnRequest) {
		
		Member member = memberRepository.findByMemberGmailAndProjectId(taskColumnRequest.getMemberGmail(), taskColumnRequest.getProjectId()).orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
		
		if(!member.getMemberRoles().stream().anyMatch(role -> role.isCanCreateColumn())) {
			throw new MemberNotAuthorizedException("Member Not Authorized to create a new column");
		}
		
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
		newTaskColumn.setTaskColumnIndex((int) (taskColumnRepository.count()) + 1);
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
	
	public List<TaskColumnResponse> getAllTaskColumnByProjectId(String projectId) {
	    List<TaskColumn> columns = taskColumnRepository.findAllByProject_ProjectId(projectId);
	    return columns.stream()
	        .map(this::mapToTaskColumnResponse)
	        .collect(Collectors.toList());
	}

	
	public TaskColumnResponse mapToTaskColumnResponse(TaskColumn column) {
	    TaskColumnResponse dto = new TaskColumnResponse();
	    dto.setTaskColumnId(column.getTaskColumnId());
	    dto.setTaskColumnName(column.getTaskColumnName());
	    dto.setTaskColumnColor(column.getTaskColumnColor());
	    dto.setProjectId(column.getProject().getProjectId());
	    dto.setWorkspaceId(column.getWorkspace().getWorkspaceId());

	    
	    List<TaskResponse> sortedTasks = column.getTasks().stream()
	            .map(this::mapToTaskResponse)
	            .sorted(Comparator.comparing(TaskResponse::getCreatedAt).reversed())
	            .collect(Collectors.toList());
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
	    dto.setGithubIssueStatus(task.getGithubIssueStatus());
	    dto.setGithubPrStatus(task.getGithubPrStatus());
	    dto.setIsIntegrated(task.getIsIntegrated());
	    dto.setGithubPullRequestLink(task.getGithubPullRequestLink());
	    dto.setPriority(task.getPriority());
	    dto.setCreatedAt(task.getCreatedAt());
	    if (task.getAssignee() != null)
	        dto.setAssignee(mapToMemberResponse(task.getAssignee()));

	    if (task.getAssignedTo() != null)
	        dto.setAssignedTo(mapToMemberResponse(task.getAssignedTo()));

	    if(task.getTags() != null) {
	    List<TagRequest> tags = task.getTags().stream()
	        .map(this::mapToTagRequest)
	        .collect(Collectors.toList());
	    dto.setTags(tags);
	    }
	    else dto.setTags(null);
	    return dto;
	}
	
	public MemberResponse mapToMemberResponse(Member member) {
	    return new MemberResponse(
	    	member.getMemberId(),
	        member.getMemberName(),
	        member.getMemberGmail(),
	        member.getMemberJoinDate()
	    );
	}
	
	public TagRequest mapToTagRequest(Tag tag) {
	    TagRequest tagDto = new TagRequest();
	    tagDto.setTitle(tag.getTitle());
	    
	    List<String> memberRoleIds = tag.getMemberRole().stream()
	        .map(role -> role.getMemberRoleId()) 
	        .collect(Collectors.toList());

	    tagDto.setMemberRoleId(memberRoleIds);
	    return tagDto;
	}
	
	public List<String> suggestTaskColumn(String projectId){
		return taskColumnRepository.findByProjectId(projectId);
	}
	
	public List<String> suggestTaskColumnAndQuery(String projectId, String query){
		String queryParam = "%" + query;
		return taskColumnRepository.findByProjectIdAndQuery(projectId, queryParam);
	}
}
