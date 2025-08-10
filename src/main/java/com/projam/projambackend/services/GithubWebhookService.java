package com.projam.projambackend.services;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projam.projambackend.dto.GithubAutomationResponse;
import com.projam.projambackend.exceptions.GithubInstallationNotFoundException;
import com.projam.projambackend.exceptions.TaskColumnNotFoundException;
import com.projam.projambackend.models.GithubInstallation;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.Task;
import com.projam.projambackend.models.TaskColumn;
import com.projam.projambackend.repositories.GithubAutomationRepository;
import com.projam.projambackend.repositories.GithubInstallationRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskColumnRepository;
import com.projam.projambackend.repositories.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class GithubWebhookService {

	private final TaskRepository taskRepository;
	private final ObjectMapper mapper;
	private final GithubAutomationRepository githubAutomationRepository;
	private final TaskColumnRepository taskColumnRepository;
	private final GithubInstallationRepository githubInstallationRepository;
	private final ProjectRepository projectRepository;

	public GithubWebhookService(TaskRepository taskRepository, GithubAutomationRepository githubAutomationRepository,
			TaskColumnRepository taskColumnRepository, GithubInstallationRepository githubInstallationRepository, ProjectRepository projectRepository) {
		this.taskRepository = taskRepository;
		this.mapper = new ObjectMapper();
		this.githubAutomationRepository = githubAutomationRepository;
		this.taskColumnRepository = taskColumnRepository;
		this.githubInstallationRepository = githubInstallationRepository;
		this.projectRepository = projectRepository;
	}

	@Transactional
	public void processWebhookEvent(String eventType, String payload) {
		if ("issues".equals(eventType)) {
			try {
				JsonNode root = mapper.readTree(payload);

				String action = root.get("action").asText();
				String issueUrl = root.get("issue").get("html_url").asText();
				String repoName = root.get("repository").get("full_name").asText();
				String title = root.get("issue").get("title").asText();

				handleGithubIssueEvent(action, issueUrl, repoName, title);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("pull_request".equals(eventType)) {
			try {
				JsonNode root = mapper.readTree(payload);
				String action = root.get("action").asText();
				String repoName = root.get("repository").get("full_name").asText();
				String title = root.get("pull_request").get("title").asText();
				String pullRequestUrl = root.get("pull_request").get("html_url").asText();
				boolean merged = false;
				if ("closed".equals(action) && root.get("pull_request").has("merged")) {
					merged = root.get("pull_request").get("merged").asBoolean();
				}
				handleGithubPullRequestEvent(action, pullRequestUrl, repoName, title, merged);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if ("installation".equals(eventType)) {
			try {
				JsonNode root = mapper.readTree(payload);
				String action = root.get("action").asText();

				if ("deleted".equals(action)) {
					Long installationId = root.get("installation").get("id").asLong();
					String accountLogin = root.get("installation").get("account").get("login").asText();

					handleUninstallationEvent(installationId, accountLogin);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void handleUninstallationEvent(Long installationId, String githubUsername) {
		GithubInstallation githubInstallation = githubInstallationRepository.findByInstallationId(installationId)
				.orElseThrow(() -> new GithubInstallationNotFoundException("Github Installation Not Found"));
		List<Project> projects = githubInstallation.getProjects();
		for(Project project : projects) {
			project.setGithubInstallation(null);
		}
		projectRepository.saveAll(projects);
		githubInstallationRepository.delete(githubInstallation);

	}

	public void handleGithubIssueEvent(String action, String issueUrl, String repoName, String title) {
		String taskKey = extractTaskKey(title);

		if (taskKey == null) {
			System.out.println("No task key found in issue title: " + title);
			return;
		}

		Optional<Task> optionalTask = taskRepository.findByTaskKey(taskKey);

		if (optionalTask.isEmpty()) {
			System.out.println("Task not found for key: " + taskKey);
			return;
		}

		Task task = optionalTask.get();
		task.setIsIntegrated(true);
		task.setGithubIssueLink(issueUrl);
		task.setGithubRepoName(repoName);
		task.setGithubIssueStatus(action);

		Project project = task.getProject();
		List<GithubAutomationResponse> githubAutomation = githubAutomationRepository
				.getAllGithubAutomationResponseByProjectId(project.getProjectId());
		if (action.equals("opened")) {
			githubAutomation.stream().filter(github -> github.getSourceEvent().equals("issue-opened")).findFirst()
					.ifPresent(github -> task.setStatus(github.getTargetColumn().replaceAll("-\\d+", "")));
		}

		TaskColumn newColumn = taskColumnRepository
				.findByTaskColumnSlugAndProject_ProjectId(task.getStatus(), project.getProjectId())
				.orElseThrow(() -> new TaskColumnNotFoundException("Task Column Not Found"));

		TaskColumn oldColumn = task.getTaskColumn();

		if (oldColumn != null) {
			oldColumn.getTasks().remove(task);
		}

		newColumn.getTasks().add(task);
		task.setTaskColumn(newColumn);
		taskColumnRepository.save(newColumn);

		taskRepository.save(task);
	}

	private String extractTaskKey(String title) {
		Pattern pattern = Pattern.compile("([A-Z]{3,4}-\\d+)");
		Matcher matcher = pattern.matcher(title);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	public void handleGithubPullRequestEvent(String action, String pullRequestUrl, String repoName, String title,
			boolean merged) {
		String taskKey = extractTaskKey(title);

		if (taskKey == null) {
			System.out.println("No task key found in issue title: " + title);
			return;
		}

		Optional<Task> optionalTask = taskRepository.findByTaskKey(taskKey);

		if (optionalTask.isEmpty()) {
			System.out.println("Task not found for key: " + taskKey);
			return;
		}

		Task task = optionalTask.get();
		task.setIsIntegrated(true);
		task.setGithubPullRequestLink(pullRequestUrl);
		task.setGithubRepoName(repoName);
		task.setGithubPrStatus(action);

		Project project = task.getProject();
		List<GithubAutomationResponse> githubAutomation = githubAutomationRepository
				.getAllGithubAutomationResponseByProjectId(project.getProjectId());

		if (action.equals("opened")) {
			githubAutomation.stream().filter(github -> github.getSourceEvent().equals("pr-opened")).findFirst()
					.ifPresent(github -> task.setStatus(github.getTargetColumn().replaceAll("-\\d+", "")));
		} else if (action.equals("closed")) {
			if (merged) {
				githubAutomation.stream().filter(github -> "merged".equals(github.getEdgeCondition())).findFirst()
						.ifPresent(github -> task.setStatus(github.getTargetColumn().replaceAll("-\\d+", "")));
			} else {
				githubAutomation.stream().filter(github -> "not_merged".equals(github.getEdgeCondition())).findFirst()
						.ifPresent(github -> task.setStatus(github.getTargetColumn().replaceAll("-\\d+", "")));
			}
		} else if (action.equals("reopened")) {
			githubAutomation.stream().filter(github -> github.getSourceEvent().equals("pr-reopened")).findFirst()
					.ifPresent(github -> task.setStatus(github.getTargetColumn().replaceAll("-\\d+", "")));
		} else if (action.equals("approved")) {
			githubAutomation.stream().filter(github -> github.getSourceEvent().equals("pr-approved")).findFirst()
					.ifPresent(github -> task.setStatus(github.getTargetColumn().replaceAll("-\\d+", "")));
		} else if (action.equals("review_requested")) {
			githubAutomation.stream().filter(github -> github.getSourceEvent().equals("review-requested")).findFirst()
					.ifPresent(github -> {
						task.setStatus(github.getTargetColumn().replaceAll("-\\d+", ""));
					});
		}

		TaskColumn newColumn = taskColumnRepository
				.findByTaskColumnSlugAndProject_ProjectId(task.getStatus(), project.getProjectId())
				.orElseThrow(() -> new TaskColumnNotFoundException("Task Column Not Found"));

		TaskColumn oldColumn = task.getTaskColumn();

		if (oldColumn != null) {
			oldColumn.getTasks().remove(task);
		}

		newColumn.getTasks().add(task);
		task.setTaskColumn(newColumn);
		taskColumnRepository.save(newColumn);
		taskRepository.save(task);
	}

}
