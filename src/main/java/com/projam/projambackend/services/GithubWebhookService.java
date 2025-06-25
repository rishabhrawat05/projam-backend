package com.projam.projambackend.services;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projam.projambackend.models.Task;
import com.projam.projambackend.repositories.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class GithubWebhookService {

	private final TaskRepository taskRepository;
	private final ObjectMapper mapper;

	public GithubWebhookService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
		this.mapper = new ObjectMapper();
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
				
				handleGithubPullRequestEvent(action, pullRequestUrl, repoName, title);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		task.setGithubStatus(action);

		taskRepository.save(task);

		System.out.println("Task " + taskKey + " updated with GitHub issue info.");
	}

	private String extractTaskKey(String title) {
		Pattern pattern = Pattern.compile("([A-Z]{3,4}-\\d+)");
		Matcher matcher = pattern.matcher(title);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	public void handleGithubPullRequestEvent(String action, String pullRequestUrl, String repoName, String title) {
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
		task.setGithubStatus(action);
		taskRepository.save(task);

		System.out.println("Task " + taskKey + " updated with GitHub pull request info.");
	}

}
