package com.projam.projambackend.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.dto.TagRequest;
import com.projam.projambackend.dto.TagResponse;
import com.projam.projambackend.dto.TaskRequest;
import com.projam.projambackend.dto.TaskResponse;
import com.projam.projambackend.dto.TaskSearchRequest;
import com.projam.projambackend.dto.TaskStatusDto;
import com.projam.projambackend.enums.ProjectStatus;
import com.projam.projambackend.exceptions.MemberNotAuthorizedException;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.TaskColumnNotFoundException;
import com.projam.projambackend.exceptions.TaskNotFoundException;
import com.projam.projambackend.models.Activity;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.Tag;
import com.projam.projambackend.models.Task;
import com.projam.projambackend.models.TaskColumn;
import com.projam.projambackend.repositories.ActivityRepository;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TagRepository;
import com.projam.projambackend.repositories.TaskColumnRepository;
import com.projam.projambackend.repositories.TaskRepository;
import jakarta.transaction.Transactional;

@Service
public class TaskService {

	private final TaskRepository taskRepository;

	private final ProjectRepository projectRepository;

	private final ActivityRepository activityRepository;

	private final MemberRepository memberRepository;

	private final TagRepository tagRepository;

	private final TaskColumnRepository taskColumnRepository;

	public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
			ActivityRepository activityRepository, MemberRepository memberRepository, TagRepository tagRepository,
			TaskColumnRepository taskColumnRepository) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.activityRepository = activityRepository;
		this.memberRepository = memberRepository;
		this.tagRepository = tagRepository;
		this.taskColumnRepository = taskColumnRepository;
	}

	@Transactional
	public TaskResponse addTask(TaskRequest taskRequest, String projectId) {
		Task task = new Task();
		Activity activity = new Activity();

		Member assignedToMember = memberRepository
				.findByMemberGmailAndProjectId(taskRequest.getAssignedTo().getMemberGmail(), projectId)
				.orElseThrow(() -> new MemberNotFoundException("Assigned Member Not Found"));
		Member assigneeMember = memberRepository
				.findByMemberGmailAndProjectId(taskRequest.getAssignee().getMemberGmail(), projectId)
				.orElseThrow(() -> new MemberNotFoundException("Assignee Member Not Found"));

		if (!assigneeMember.getMemberRoles().stream().anyMatch(role -> role.isCanCreateTask())) {
			throw new MemberNotAuthorizedException("Member Not Authorized To Create Task");
		}

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		TaskColumn taskColumn = taskColumnRepository
				.findByTaskColumnSlugAndProject_ProjectId(taskRequest.getTaskColumnSlug(), projectId)
				.orElseThrow(() -> new TaskColumnNotFoundException("Task Column Not Found"));
		task.setTitle(taskRequest.getTitle());
		task.setDescription(taskRequest.getDescription());
		task.setStartDate(taskRequest.getStartDate());
		task.setEndDate(taskRequest.getEndDate());
		task.setStatus(taskRequest.getStatus());
		task.setTaskNumber(taskRepository.countByProject(project) + 1);
		task.setAssignee(assigneeMember);
		task.setAssignedTo(assignedToMember);
		task.setPriority(taskRequest.getPriority());
		task.setIsDeleted(false);
		task.setCreatedAt(LocalDateTime.now());
		if (assignedToMember != null) {
			task.setAssignedAt(LocalDateTime.now());
		}
		assigneeMember.addTaskAssignedTo(task);
		assignedToMember.addAssignedTask(task);
		task.setTaskColumn(taskColumn);
		taskColumn.getTasks().add(task);
		task.setProject(project);
		String cleanedName = project.getProjectName().replaceAll("[^a-zA-Z]", "").toUpperCase();
		String projectPrefix = cleanedName.substring(0, Math.min(4, cleanedName.length()));
		String taskKey = projectPrefix + "-" + task.getTaskNumber();
		task.setTaskKey(taskKey);

		List<TagResponse> tagRequests = taskRequest.getTags();
		if (tagRequests != null) {
			for (TagResponse tagR : tagRequests) {
				Tag tag = tagRepository.findById(tagR.getTagId())
						.orElseThrow(() -> new RuntimeException("Tag not found"));
				task.addTag(tag);
			}
		}
		taskRepository.save(task);
		memberRepository.saveAll(List.of(assigneeMember, assignedToMember));
		taskColumnRepository.save(taskColumn);
		LocalDateTime now = LocalDateTime.now();
		activity.setDescription("A new Task has been created by " + assigneeMember.getMemberName());
		activity.setTimeStamp(now);
		activity.setProject(project);
		activity.setTask(task);
		activity.setMember(assigneeMember);
		activityRepository.save(activity);

		if (taskRepository.countByProject(project) >= 1) {
			project.setProjectStatus(ProjectStatus.IN_PROGRESS);
			projectRepository.save(project);
		}

		return taskToTaskResponse(task);
	}

	public TaskResponse taskToTaskResponse(Task task) {
		TaskResponse taskResponse = new TaskResponse();
		taskResponse.setTitle(task.getTitle());
		taskResponse.setDescription(task.getDescription());
		taskResponse.setStatus(task.getStatus());
		taskResponse.setTaskNumber(task.getTaskNumber());
		taskResponse.setPriority(task.getPriority());
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
		taskResponse.setTaskKey(task.getTaskKey());
		taskResponse.setGithubIssueLink(task.getGithubIssueLink());
		taskResponse.setGithubRepoName(task.getGithubRepoName());
		taskResponse.setGithubIssueStatus(task.getGithubIssueStatus());
		taskResponse.setGithubPrStatus(task.getGithubPrStatus());
		taskResponse.setIsIntegrated(task.getIsIntegrated());
		taskResponse.setGithubPullRequestLink(task.getGithubPullRequestLink());
		taskResponse.setTaskColumnId(task.getTaskColumn().getTaskColumnId());
		if (task.getTaskId() != null) {
			taskResponse.setTaskId(task.getTaskId());
		}
		List<Tag> tags = task.getTags();
		if (tags != null) {
			List<TagRequest> tagResponse = new ArrayList<>();
			for (Tag tag : tags) {
				TagRequest tagR = new TagRequest();
				tagR.setTitle(tag.getTitle());
				tagResponse.add(tagR);
			}
			taskResponse.setTags(tagResponse);
		} else {
			taskResponse.setTags(null);
		}
		return taskResponse;
	}

	public Page<TaskResponse> getAllTasksByProjectId(String projectId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Task> tasks = taskRepository.findAllByProjectId(projectId, pageable);
		return tasks.map(this::taskToTaskResponse);
	}

	@Transactional
	public TaskResponse updateTaskStatus(TaskStatusDto taskStatusDto, String projectId) {
		Task task = taskRepository.findByIdAndProjectId(taskStatusDto.getTaskId(), projectId)
				.orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
		task.setStatus(taskStatusDto.getStatus());
		if (taskStatusDto.getStatus().equals("completed")) {
			task.setCompletedAt(LocalDateTime.now());
		}
		Activity activity = new Activity();
		activity.setDescription(task.getTaskKey() + " status has been updated to " + taskStatusDto.getStatus());
		activity.setTimeStamp(LocalDateTime.now());
		activity.setProject(projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project Not Found")));
		activity.setTask(task);
		if (taskStatusDto.getStatus() != null) {
			TaskColumn newColumn = taskColumnRepository
					.findByTaskColumnSlugAndProject_ProjectId(taskStatusDto.getStatus(), projectId)
					.orElseThrow(() -> new TaskColumnNotFoundException("Task Column Not Found"));

			TaskColumn oldColumn = task.getTaskColumn();

			if (oldColumn != null) {
				oldColumn.getTasks().remove(task);
			}

			newColumn.getTasks().add(task);
			task.setTaskColumn(newColumn);
			taskColumnRepository.save(newColumn);
		}

		activityRepository.save(activity);
		taskRepository.save(task);

		return taskToTaskResponse(task);

	}

	public Page<TaskResponse> getPaginatedTasks(String columnId, String projectId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Task> tasks = taskRepository.findAllByTaskColumn_TaskColumnIdAndTaskColumn_Project_ProjectId(columnId,
				projectId, pageable);
		return tasks.map(this::taskToTaskResponse);
	}

	public void deleteTask(String taskId) {
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
		task.setIsDeleted(true);
		taskRepository.save(task);
	}

	public List<String> suggestTitleByProjectId(String projectId) {
		return taskRepository.findTitleByProjectId(projectId);
	}

	public List<String> suggestTitleByProjectIdAndQuery(String projectId, String query) {
		String queryParam = "%" + query;
		return taskRepository.findTitleByProjectIdAndQuery(projectId, queryParam);
	}

	public List<TaskResponse> getTaskByQuery(String projectId, TaskSearchRequest taskSearchRequest) {
	    Integer priority = null;
	    LocalDate due = null;
	    LocalDate dueStart = null;
	    LocalDate dueEnd = null;

	    if (taskSearchRequest.getPriority() != null) {
	        priority = switch (taskSearchRequest.getPriority()) {
	            case "critical" -> 1;
	            case "high" -> 2;
	            case "medium" -> 3;
	            case "low" -> 4;
	            default -> null;
	        };
	    }

	    if (taskSearchRequest.getDue() != null) {
	        switch (taskSearchRequest.getDue()) {
	            case "today" -> due = LocalDate.now();
	            case "tomorrow" -> due = LocalDate.now().plusDays(1);
	            case "next week" -> due = LocalDate.now().plusWeeks(1);
	            case "next month" -> {
	                LocalDate now = LocalDate.now();
	                dueStart = now.plusMonths(1).withDayOfMonth(1);
	                dueEnd = dueStart.plusMonths(1).minusDays(1);
	            }
	        }
	    }

	    List<String> tags = taskSearchRequest.getTags();
	    if (tags != null && tags.isEmpty()) {
	        tags = null;
	    }

	    List<Task> tasks = taskRepository.findTaskCardsByQuery(
	        projectId,
	        taskSearchRequest.getMemberName(),
	        due,
	        dueStart,
	        dueEnd,
	        taskSearchRequest.getTitle(),
	        priority,
	        taskSearchRequest.getStatus(),
	        tags
	    );

	    return tasks.stream()
	        .map(task -> new TaskResponse(
	            task.getTitle(),
	            task.getTaskId(),
	            task.getTaskColumn().getTaskColumnId(),
	            task.getTaskKey(),
	            task.getEndDate(),
	            task.getPriority(),
	            task.getTags().stream().map(Tag::getTitle).collect(Collectors.toList())
	        ))
	        .collect(Collectors.toList());
	}
	
	public Long getCountByTaskColumnIdAndProjectId(String projectId, String taskColumnId) {
		return taskRepository.countByProjectIdAndTaskColumnId(projectId, taskColumnId);
	}

}
