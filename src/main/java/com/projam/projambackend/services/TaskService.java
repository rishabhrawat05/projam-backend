package com.projam.projambackend.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.dto.TagResponse;
import com.projam.projambackend.dto.TaskRequest;
import com.projam.projambackend.dto.TaskResponse;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.models.Activity;
import com.projam.projambackend.models.Tag;
import com.projam.projambackend.models.Task;
import com.projam.projambackend.repositories.ActivityRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskRepository;

@Service
public class TaskService {


	private final TaskRepository taskRepository;
	
	private final ProjectRepository projectRepository;
	
	private final ActivityRepository activityRepository;
	
	public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, ActivityRepository activityRepository) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.activityRepository = activityRepository;
	}
	
	public TaskResponse addTask(TaskRequest taskRequest, Long projectId) {
		Task task = new Task();
		Activity activity = new Activity();
		task.setTitle(taskRequest.getTitle());
		task.setDescription(taskRequest.getDescription());
		task.setStartDate(taskRequest.getStartDate());
		task.setEndDate(taskRequest.getEndDate());
		task.setStatus(taskRequest.getStatus());
		task.setAssignee(taskRequest.getAssignee());
		task.setAssignedTo(taskRequest.getAssignedTo());
		task.setProject(projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project Not Found")));
		task.setTags(taskRequest.getTags());
		activity.setDescription("A new Task has been created by " + taskRequest.getAssignee() + " at " + LocalDateTime.now());
		activity.setTimeStamp(LocalDateTime.now());
		taskRepository.save(task);
		activityRepository.save(activity);
		return taskToTaskResponse(task);
		
	}
	
	public TaskResponse taskToTaskResponse(Task task) {
		TaskResponse taskResponse = new TaskResponse();
		taskResponse.setTitle(task.getTitle());
		taskResponse.setDescription(task.getDescription());
		taskResponse.setStatus(task.getStatus());
		MemberResponse assignee = new MemberResponse();
		assignee.setMemberGmail(task.getAssignee().getMemberGmail());
		assignee.setMemberJoinDate(task.getAssignee().getMemberJoinDate());
		assignee.setMemberName(task.getAssignee().getMemberName());
		
		MemberResponse assignedTo = new MemberResponse();
		assignedTo.setMemberGmail(task.getAssignedTo().getMemberGmail());
		assignedTo.setMemberJoinDate(task.getAssignedTo().getMemberJoinDate());
		assignedTo.setMemberName(task.getAssignedTo().getMemberName());
		taskResponse.setAssignee(assignee);
		taskResponse.setAssignedTo(assignedTo);
		taskResponse.setStartDate(task.getStartDate());
		taskResponse.setEndDate(task.getEndDate());
		Set<Tag> tags = task.getTags();
		Set<TagResponse> tagResponse = new HashSet<>();
		for(Tag tag : tags) {
			TagResponse tagR = new TagResponse();
			tagR.setTitle(tag.getTitle());
			tagResponse.add(tagR);
		}
		taskResponse.setTags(tagResponse);
		return taskResponse;
	}
	
	public Page<TaskResponse> getAllTasksByProjectId(Long projectId, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		Page<Task> tasks =  taskRepository.findAllByProjectId(projectId, pageable);
		return tasks.map(this::taskToTaskResponse);
	}
}
