package com.projam.projambackend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.JoinWorkspaceRequest;
import com.projam.projambackend.dto.WorkspaceRequest;
import com.projam.projambackend.dto.WorkspaceResponse;
import com.projam.projambackend.email.EmailUtility;
import com.projam.projambackend.exceptions.JoinWorkspaceTokenAlreadyUsedException;
import com.projam.projambackend.exceptions.JoinWorkspaceTokenExpiredException;
import com.projam.projambackend.exceptions.UserNotFoundException;
import com.projam.projambackend.exceptions.WorkspaceAlreadyPresentException;
import com.projam.projambackend.exceptions.WorkspaceInviteNotAllowedException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.JoinWorkspaceToken;
import com.projam.projambackend.models.User;
import com.projam.projambackend.models.Workspace;
import com.projam.projambackend.repositories.JoinWorkspaceTokenRepository;
import com.projam.projambackend.repositories.UserRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

import jakarta.transaction.Transactional;

@Service
public class WorkspaceService {

	private final WorkspaceRepository workspaceRepository;

	private final EmailUtility emailUtility;
	
	private final JoinWorkspaceTokenRepository joinWorkspaceTokenRepository;
	
	private final UserRepository userRepository;

	public WorkspaceService(WorkspaceRepository workspaceRepository, EmailUtility emailUtility, JoinWorkspaceTokenRepository joinWorkspaceTokenRepository, UserRepository userRepository) {
		this.workspaceRepository = workspaceRepository;
		this.emailUtility = emailUtility;
		this.joinWorkspaceTokenRepository = joinWorkspaceTokenRepository;
		this.userRepository = userRepository;
	}

	public WorkspaceResponse createWorkspace(WorkspaceRequest workspaceRequest) {
		Optional<Workspace> workspaceOpt = workspaceRepository.findByWorkspaceName(workspaceRequest.getWorkspaceName());
		if (workspaceOpt.isPresent()) {
			throw new WorkspaceAlreadyPresentException(
					"Workspace with the name " + workspaceRequest.getWorkspaceName() + " is already present");
		}
		Workspace newWorkspace = new Workspace();
		newWorkspace.setWorkspaceName(workspaceRequest.getWorkspaceName());
		newWorkspace.setOrganizationName(workspaceRequest.getOrganizationName());
		newWorkspace.setWorkspaceType(workspaceRequest.getWorkspaceType());
		newWorkspace.setUsers(workspaceRequest.getUsers());
		String slug = generateWorkspaceSlug(workspaceRequest.getWorkspaceName());
		String uniqueSlug = slug;
		int counter = 1;
		while (workspaceRepository.findByWorkspaceSlug(slug).isPresent()) {
			uniqueSlug = slug + "-" + counter++;
		}
		newWorkspace.setWorkspaceSlug(uniqueSlug);
		workspaceRepository.save(newWorkspace);
		return workspaceToWorkspaceResponse(newWorkspace);
	}

	public WorkspaceResponse updateWorkspace(WorkspaceRequest workspaceRequest, Long workspaceId) {
		Optional<Workspace> workspaceOpt = workspaceRepository.findById(workspaceId);
		if (workspaceOpt.isEmpty()) {
			throw new WorkspaceNotFoundException("Workspace with the id " + workspaceId + " is not found");
		}
		Workspace workspace = workspaceOpt.get();
		if (workspaceRequest.getOrganizationName() != null) {
			workspace.setOrganizationName(workspaceRequest.getOrganizationName());
		}

		if (workspaceRequest.getWorkspaceName() != null) {
			workspace.setWorkspaceName(workspaceRequest.getWorkspaceName());
		}

		if (workspaceRequest.getWorkspaceType() != null) {
			workspace.setWorkspaceType(workspaceRequest.getWorkspaceType());
		}

		if (workspaceRequest.getUsers() != null) {
			Set<User> workspaceUsers = workspace.getUsers();

			for (User newUser : workspaceRequest.getUsers()) {
				if (!workspaceUsers.contains(newUser)) {
					workspace.addUser(newUser);
				}
			}
		}

		workspaceRepository.save(workspace);

		return workspaceToWorkspaceResponse(workspace);

	}

	public WorkspaceResponse workspaceToWorkspaceResponse(Workspace workspace) {
		WorkspaceResponse workspaceResponse = new WorkspaceResponse();
		workspaceResponse.setOrganizationName(workspace.getOrganizationName());
		workspaceResponse.setUsers(workspace.getUsers());
		workspaceResponse.setWorkspaceName(workspace.getWorkspaceName());
		workspaceResponse.setWorkspaceType(workspace.getWorkspaceType());
		if (workspace.getWorkspaceSlug() != null) {
			workspaceResponse.setWorkspaceSlug(workspace.getWorkspaceSlug());
		}
		return workspaceResponse;
	}

	public String deleteWorkspaceById(Long workspaceId) {
		Optional<Workspace> workspaceOpt = workspaceRepository.findById(workspaceId);
		if (workspaceOpt.isEmpty()) {
			throw new WorkspaceNotFoundException("Workspace with the id " + workspaceId + " is not found.");
		}

		Workspace workspace = workspaceOpt.get();
		workspaceRepository.delete(workspace);

		return "Workspace has been deleted Successfully";
	}

	public Workspace getWorkspaceById(Long workspaceId) {
		Optional<Workspace> workspaceOpt = workspaceRepository.findById(workspaceId);
		if (workspaceOpt.isEmpty()) {
			throw new WorkspaceNotFoundException("Workspace with the id " + workspaceId + " is not found.");
		}

		return workspaceOpt.get();
	}

	public List<Workspace> getAllWorkspaces() {
		List<Workspace> workspaceList = workspaceRepository.findAll();
		return workspaceList;
	}

	public String generateWorkspaceSlug(String workspaceName) {
		return workspaceName.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
	}

	public String joinWorkspace(JoinWorkspaceRequest joinWorkspaceRequest) {
		Optional<Workspace> workspaceOpt = workspaceRepository.findByWorkspaceSlug(joinWorkspaceRequest.getWorkspaceSlug());
		if (workspaceOpt.isEmpty()) {
			throw new WorkspaceNotFoundException("Workspace Not Found with slug " + joinWorkspaceRequest.getWorkspaceSlug());
		}
		Workspace workspace = workspaceOpt.get();
		if (!workspace.getIsAllowedInvites()) {
			throw new WorkspaceInviteNotAllowedException("Sorry but the workspace not allow invites");
		}
		
		String magicLink = createMagicJoinLink(workspace, joinWorkspaceRequest.getGmail());
		emailUtility.sendEmail(
				joinWorkspaceRequest.getGmail(),
				"ProJam Workspace Invite: " + workspace.getWorkspaceName(),
				"Click the link below to join the workspace:\n" + magicLink
			);
		return "Invite link sent successfully to " + joinWorkspaceRequest.getGmail();

	}

	public String createMagicJoinLink(Workspace workspace, String gmail) {
		String token = UUID.randomUUID().toString();
		JoinWorkspaceToken joinToken = new JoinWorkspaceToken();
		joinToken.setToken(token);
		joinToken.setWorkspace(workspace);
		joinToken.setExpiresAt(LocalDateTime.now().plusDays(2));
		joinToken.setUsed(false);
		joinToken.setEmail(gmail);
		joinWorkspaceTokenRepository.save(joinToken);

		return "https://projam.app/join/workspace/" + token;
	}
	
	@Transactional
	public String joinWorkspaceWithInviteLink(String token) {
		JoinWorkspaceToken joinWorkspaceToken = joinWorkspaceTokenRepository.findByToken(token).orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));
		if(joinWorkspaceToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new JoinWorkspaceTokenExpiredException("The join workspace token has been expired");
		}
		if(joinWorkspaceToken.getUsed()) {
			throw new JoinWorkspaceTokenAlreadyUsedException("Invite Link can be used only once and it has been already Used");
		}
		User user = userRepository.findByGmail(joinWorkspaceToken.getEmail()).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		Workspace workspace = joinWorkspaceToken.getWorkspace();
		if (!user.getWorkspaces().contains(workspace)) {
	        user.getWorkspaces().add(workspace);
	        workspace.getUsers().add(user);
	    }
		else {
			return "Workspace is already Joined";
		}
		joinWorkspaceToken.setUsed(true);
		joinWorkspaceToken.setToken(null);
		workspace.addUser(user);
		userRepository.save(user);
		workspaceRepository.save(workspace);
		joinWorkspaceTokenRepository.save(joinWorkspaceToken);
		return "Workspace joined using the invite link";
		
		
	}
}
