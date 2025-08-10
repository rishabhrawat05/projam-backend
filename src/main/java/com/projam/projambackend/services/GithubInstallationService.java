package com.projam.projambackend.services;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.projam.projambackend.dto.GithubInstallationResponse;
import com.projam.projambackend.exceptions.GithubInstallationNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.GithubAutomation;
import com.projam.projambackend.models.GithubInstallation;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.TaskColumn;
import com.projam.projambackend.models.Workspace;
import com.projam.projambackend.repositories.GithubInstallationRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskColumnRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;

@Service
public class GithubInstallationService {

	private final GithubInstallationRepository githubInstallationRepository;
	private final ProjectRepository projectRepository;
	private final WorkspaceRepository workspaceRepository;
	private final TaskColumnRepository taskColumnRepository;

	@Value("${github.app.id}")
	private String githubAppId;
	
	@Value("${github.private.key}")
	private String GITHUB_PRIVATE_KEY;

	public GithubInstallationService(GithubInstallationRepository githubInstallationRepository,
			ProjectRepository projectRepository, WorkspaceRepository workspaceRepository,
			TaskColumnRepository taskColumnRepository) {
		this.githubInstallationRepository = githubInstallationRepository;
		this.projectRepository = projectRepository;
		this.workspaceRepository = workspaceRepository;
		this.taskColumnRepository = taskColumnRepository;
	}

	public RSAPrivateKey loadPrivateKey() throws Exception {
		String privateKeyPem = GITHUB_PRIVATE_KEY;
	    if (privateKeyPem == null) {
	        throw new RuntimeException("GITHUB_PRIVATE_KEY env var not set");
	    }
	    
	    String content = privateKeyPem
	            .replace("-----BEGIN PRIVATE KEY-----", "")
	            .replace("-----END PRIVATE KEY-----", "")
	            .replaceAll("\\s", "");

		byte[] keyBytes = Base64.getDecoder().decode(content);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
	}

	public String generateGitHubAppJWT() throws Exception {
		RSAPrivateKey privateKey = loadPrivateKey();
		long now = System.currentTimeMillis();

		return Jwts.builder().setIssuer(githubAppId).setIssuedAt(new Date(now)).setExpiration(new Date(now + 600_000))
				.signWith(privateKey, SignatureAlgorithm.RS256).compact();
	}

	@Transactional
	public void saveInstallation(String installationIdStr, String workspaceId, String projectId, String userEmail) throws Exception {
		Long installationId = Long.parseLong(installationIdStr);

		Workspace workspace = workspaceRepository.findById(workspaceId)
				.orElseThrow(() -> new RuntimeException("Workspace not found"));

		GithubInstallation existingInstallation = githubInstallationRepository.findByInstallationId(installationId)
				.orElse(null);

		if(!workspace.getAdminGmail().equals(userEmail)) {
			throw new GithubInstallationNotFoundException("Github Save Installation is not authorized");
		}
		
		if (existingInstallation != null) {
		    GithubInstallation newLink = new GithubInstallation();
		    newLink.setInstallationId(existingInstallation.getInstallationId());
		    newLink.setAccessToken(existingInstallation.getAccessToken());
		    newLink.setGithubUsername(existingInstallation.getGithubUsername());
		    newLink.setConnectedAt(Instant.now());
		    newLink.setAdminGmail(workspace.getAdminGmail());

		    githubInstallationRepository.save(newLink);
		    return;
		}
		String jwt = generateGitHubAppJWT();
		System.out.println(jwt);
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(jwt);
		headers.set("Accept", "application/vnd.github+json");
		headers.set("User-Agent", "ProJamm");
		headers.set("X-GitHub-Api-Version", "2022-11-28");
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		System.out.println(headers);
		ResponseEntity<Map> response = restTemplate.exchange(
				"https://api.github.com/app/installations/" + installationId + "/access_tokens", HttpMethod.POST,
				entity, Map.class);

		String accessToken = (String) response.getBody().get("token");

		HttpHeaders authHeaders = new HttpHeaders();
		authHeaders.setBearerAuth(accessToken);
		authHeaders.setAccept(Collections.singletonList(MediaType.valueOf("application/vnd.github+json")));
		HttpEntity<Void> authEntity = new HttpEntity<>(authHeaders);

		ResponseEntity<Map> reposResponse = restTemplate.exchange("https://api.github.com/installation/repositories",
				HttpMethod.GET, authEntity, Map.class);

		List<Map<String, Object>> repositories = (List<Map<String, Object>>) reposResponse.getBody()
				.get("repositories");

		if (repositories.isEmpty()) {
			throw new RuntimeException("No repositories found for this installation.");
		}

		Map<String, Object> firstRepo = repositories.get(0);
		Map<String, Object> owner = (Map<String, Object>) firstRepo.get("owner");
		String githubUsername = (String) owner.get("login");

		GithubInstallation installation = new GithubInstallation();
		installation.setInstallationId(installationId);
		installation.setAccessToken(accessToken);
		installation.setGithubUsername(githubUsername);
		installation.setConnectedAt(Instant.now());
		installation.setAdminGmail(workspace.getAdminGmail());
		githubInstallationRepository.save(installation);
	}

	public String refreshAccessToken(GithubInstallation installation) throws Exception {
		String jwt = generateGitHubAppJWT();
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(jwt);
		headers.set("Accept", "application/vnd.github+json");

		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<Map> response = restTemplate.exchange(
				"https://api.github.com/app/installations/" + installation.getInstallationId() + "/access_tokens",
				HttpMethod.POST, entity, Map.class);

		String newAccessToken = (String) response.getBody().get("token");
		installation.setAccessToken(newAccessToken);
		githubInstallationRepository.save(installation);

		return newAccessToken;
	}

	public List<Map<String, Object>> getAuthorizedRepos(String workspaceId) throws Exception {
		Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));
	    GithubInstallation installation = githubInstallationRepository.findByAdminGmail(workspace.getAdminGmail())
	            .orElseThrow(() -> new RuntimeException("GitHub Installation not found"));

	    RestTemplate restTemplate = new RestTemplate();

	    try {
	        return fetchReposWithToken(installation, installation.getAccessToken(), restTemplate);
	    } catch (HttpClientErrorException.Unauthorized ex) {
	        String newAccessToken = refreshAccessToken(installation);
	        return fetchReposWithToken(installation, newAccessToken, restTemplate);
	    }
	}

	private List<Map<String, Object>> fetchReposWithToken(GithubInstallation installation, String token, RestTemplate restTemplate) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(token);
	    headers.set("Accept", "application/vnd.github+json");

	    HttpEntity<Void> entity = new HttpEntity<>(headers);

	    ResponseEntity<Map> response = restTemplate.exchange(
	            "https://api.github.com/installation/repositories",
	            HttpMethod.GET, entity, Map.class);

	    return (List<Map<String, Object>>) response.getBody().get("repositories");
	}


	@Transactional
	public void connectRepoToProject(String projectId, String repoName, String repoOwner) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));

		if (project.getGithubInstallation() != null &&
			    project.getLinkedRepoName() != null &&
			    project.getLinkedRepoOwner() != null) {
			    throw new RuntimeException("Project already connected to a repo");
			}


		GithubInstallation installation = githubInstallationRepository
				.findByAdminGmail(project.getWorkspace().getAdminGmail())
				.orElseThrow(() -> new RuntimeException("GitHub Installation not found for this workspace"));

		project.setGithubInstallation(installation);
		project.setLinkedRepoName(repoName);
		project.setLinkedRepoOwner(repoOwner);

		List<GithubAutomation> newEdges = List.of(new GithubAutomation("pr-opened", "inprogress-1", "#fcd34d", project),
				new GithubAutomation("pr-closed", "todo-0", "#4e8de5", "not_merged", project),
				new GithubAutomation("pr-closed", "completed-2", "#56c596", "merged", project),
				new GithubAutomation("issue-opened", "todo-0", "#4e8de5", project),
				new GithubAutomation("review-requested", "inreview-3", "#b894fc", project),
				new GithubAutomation("pr-reopened", "inprogress-1", "#fcd34d", project),
				new GithubAutomation("pr-approved", "readytomerge-4", "#b894fc", project),
				new GithubAutomation("issue-closed", "completed-2", "#56c596", project));
		project.getEdges().clear();
		project.getEdges().addAll(newEdges);

		Optional<TaskColumn> inReviewOpt = taskColumnRepository.findByTaskColumnSlugAndProject_ProjectId("inreview",
				projectId);

		if (inReviewOpt.isEmpty()) {
			TaskColumn inReview = new TaskColumn();
			inReview.setProject(project);
			inReview.setTaskColumnColor("bg-ppurple-light");
			inReview.setTaskColumnIndex(taskColumnRepository.countByProject_ProjectId(projectId)+1);
			inReview.setTaskColumnName("In Review");
			inReview.setTaskColumnSlug("inreview");
			inReview.setWorkspace(project.getWorkspace());
			taskColumnRepository.save(inReview);
		}

		Optional<TaskColumn> inReadyToMergeOpt = taskColumnRepository.findByTaskColumnSlugAndProject_ProjectId("readytomerge",
				projectId);

		if (inReadyToMergeOpt.isEmpty()) {
			TaskColumn readyToMerge = new TaskColumn();
			readyToMerge.setProject(project);
			readyToMerge.setTaskColumnColor("bg-ppurple-light");
			readyToMerge.setTaskColumnIndex(taskColumnRepository.countByProject_ProjectId(projectId)+1);
			readyToMerge.setTaskColumnName("Ready To Merge");
			readyToMerge.setTaskColumnSlug("readytomerge");
			readyToMerge.setWorkspace(project.getWorkspace());
			taskColumnRepository.save(readyToMerge);
		}
		
		

		projectRepository.save(project);
	}

	public GithubInstallationResponse isProjectConnected(String projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));

		GithubInstallationResponse response = new GithubInstallationResponse();

		if (project.getGithubInstallation() != null && project.getLinkedRepoName() != null
				&& project.getLinkedRepoOwner() != null) {

			response.setConnected(true);
			response.setRepoName(project.getLinkedRepoName());
			response.setRepoOwner(project.getLinkedRepoOwner());
		} else {
			response.setConnected(false);
		}

		return response;
	}
	
	@Transactional
	public void disconnectRepoFromProject(String projectId, String userEmail) {
	    Project project = projectRepository.findById(projectId)
	        .orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));

	    if (!project.getMembers().stream().anyMatch(m -> m.getMemberGmail().equals(userEmail))) {
	        throw new AccessDeniedException("You are not authorized to disconnect this repo");
	    }

	    if (project.getGithubInstallation() == null) {
	        throw new IllegalStateException("No GitHub repository is currently connected to this project.");
	    }

	    project.setLinkedRepoName(null);
	    project.setLinkedRepoOwner(null);

	    project.getEdges().removeIf(edge -> edge instanceof GithubAutomation);

	    project.getTasks().forEach(task -> {
	    	task.setIsIntegrated(false);
	    });

	    projectRepository.save(project);
	}

}
