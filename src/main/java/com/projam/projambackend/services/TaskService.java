package com.projam.projambackend.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.MemberRequest;
import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.dto.TagRequest;
import com.projam.projambackend.dto.TaskRequest;
import com.projam.projambackend.dto.TaskResponse;
import com.projam.projambackend.dto.TaskStatusDto;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.MemberRoleNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.TaskColumnNotFoundException;
import com.projam.projambackend.exceptions.TaskNotFoundException;
import com.projam.projambackend.models.Activity;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.MemberRole;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.Tag;
import com.projam.projambackend.models.Task;
import com.projam.projambackend.models.TaskColumn;
import com.projam.projambackend.repositories.ActivityRepository;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.MemberRoleRepository;
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

	private final MemberRoleRepository memberRoleRepository;

	private final TaskColumnRepository taskColumnRepository;

	public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
			ActivityRepository activityRepository, MemberRepository memberRepository, TagRepository tagRepository,
			MemberRoleRepository memberRoleRepository, TaskColumnRepository taskColumnRepository) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.activityRepository = activityRepository;
		this.memberRepository = memberRepository;
		this.tagRepository = tagRepository;
		this.memberRoleRepository = memberRoleRepository;
		this.taskColumnRepository = taskColumnRepository;
	}

	@Transactional
	public TaskResponse addTask(TaskRequest taskRequest, Long projectId) {
		Task task = new Task();
		Activity activity = new Activity();

		Member assignedToMember = memberRepository
				.findByMemberGmailAndProjectId(taskRequest.getAssignedTo().getMemberGmail(), projectId)
				.orElseThrow(() -> new MemberNotFoundException("Assigned Member Not Found"));
		Member assigneeMember = memberRepository
				.findByMemberGmailAndProjectId(taskRequest.getAssignee().getMemberGmail(), projectId)
				.orElseThrow(() -> new MemberNotFoundException("Assignee Member Not Found"));
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
		assigneeMember.addTaskAssignedTo(task);
		assignedToMember.addAssignedTask(task);
		task.setTaskColumn(taskColumn);
		taskColumn.getTasks().add(task);
		task.setProject(project);
		String cleanedName = project.getProjectName().replaceAll("[^a-zA-Z]", "").toUpperCase();
		String projectPrefix = cleanedName.substring(0, Math.min(4, cleanedName.length()));
		String taskKey = projectPrefix + "-" + task.getTaskNumber();
		task.setTaskKey(taskKey);

		Set<TagRequest> tagRequests = taskRequest.getTags();
		if (tagRequests != null) {
			for (TagRequest tagR : tagRequests) {
				Tag tag = tagRepository.findByTitle(tagR.getTitle()).orElseGet(() -> new Tag());
				tag.setTitle(tagR.getTitle());
				Set<MemberRole> roles = tagR.getMemberRoleId().stream()
						.map(id -> memberRoleRepository.findById(id)
								.orElseThrow(() -> new MemberRoleNotFoundException("Member Role Not Found")))
						.collect(Collectors.toSet());

				tag.setMemberRole(roles);
				tag = tagRepository.save(tag);

				task.addTag(tag);
			}
		} else {
			task.setTags(null);
		}
		taskRepository.save(task);
		memberRepository.saveAll(List.of(assigneeMember, assignedToMember));
		taskColumnRepository.save(taskColumn);
		LocalDateTime now = LocalDateTime.now();
		activity.setDescription("A new Task has been created by " + assigneeMember.getMemberName() + " at " + now);
		activity.setTimeStamp(now);
		activity.setProject(project);
		activity.setTask(task);
		activity.setMember(assigneeMember);
		activityRepository.save(activity);

		return taskToTaskResponse(task);
	}

	public TaskResponse taskToTaskResponse(Task task) {
		TaskResponse taskResponse = new TaskResponse();
		taskResponse.setTitle(task.getTitle());
		taskResponse.setDescription(task.getDescription());
		taskResponse.setStatus(task.getStatus());
		taskResponse.setTaskNumber(task.getTaskNumber());
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
		taskResponse.setGithubStatus(task.getGithubStatus());
		taskResponse.setIsIntegrated(task.getIsIntegrated());
		taskResponse.setGithubPullRequestLink(task.getGithubPullRequestLink());
		if (task.getTaskId() != null) {
			taskResponse.setTaskId(task.getTaskId());
		}
		Set<Tag> tags = task.getTags();
		if (tags != null) {
			Set<TagRequest> tagResponse = new HashSet<>();
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

	public Page<TaskResponse> getAllTasksByProjectId(Long projectId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Task> tasks = taskRepository.findAllByProjectId(projectId, pageable);
		return tasks.map(this::taskToTaskResponse);
	}

	@Transactional
	public TaskResponse updateTaskStatus(TaskStatusDto taskStatusDto, Long projectId) {
		Task task = taskRepository.findByIdAndProjectId(taskStatusDto.getTaskId(), projectId)
				.orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
		task.setStatus(taskStatusDto.getStatus());
		Activity activity = new Activity();
		activity.setDescription(
				"Task-" + task.getTaskNumber() + " status has been updated to " + taskStatusDto.getStatus());
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
}
