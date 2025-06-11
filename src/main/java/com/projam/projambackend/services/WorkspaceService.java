package com.projam.projambackend.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.JoinWorkspaceRequestDto;
import com.projam.projambackend.dto.JoinWorkspaceRequestResponse;
import com.projam.projambackend.dto.UserResponse;
import com.projam.projambackend.dto.WorkspaceRequest;
import com.projam.projambackend.dto.WorkspaceResponse;
import com.projam.projambackend.dto.WorkspaceSummaryDto;
import com.projam.projambackend.email.EmailUtility;
import com.projam.projambackend.exceptions.JoinWorkspaceRequestAlreadyExistException;
import com.projam.projambackend.exceptions.JoinWorkspaceRequestNotFound;
import com.projam.projambackend.exceptions.JoinWorkspaceTokenAlreadyUsedException;
import com.projam.projambackend.exceptions.JoinWorkspaceTokenExpiredException;
import com.projam.projambackend.exceptions.UserNotFoundException;
import com.projam.projambackend.exceptions.WorkspaceAlreadyPresentException;
import com.projam.projambackend.exceptions.WorkspaceInviteNotAllowedException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.JoinWorkspaceRequest;
import com.projam.projambackend.models.JoinWorkspaceToken;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.User;
import com.projam.projambackend.models.Workspace;
import com.projam.projambackend.repositories.JoinWorkspaceRequestRepository;
import com.projam.projambackend.repositories.JoinWorkspaceTokenRepository;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.UserRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

import jakarta.transaction.Transactional;

@Service
public class WorkspaceService {

	private final WorkspaceRepository workspaceRepository;

	private final EmailUtility emailUtility;
	
	private final JoinWorkspaceTokenRepository joinWorkspaceTokenRepository;
	
	private final UserRepository userRepository;
	
	private final JoinWorkspaceRequestRepository joinWorkspaceRequestRepository;
	
	private final MemberRepository memberRepository;

	public WorkspaceService(WorkspaceRepository workspaceRepository, EmailUtility emailUtility, JoinWorkspaceTokenRepository joinWorkspaceTokenRepository, UserRepository userRepository, JoinWorkspaceRequestRepository joinWorkspaceRequestRepository, MemberRepository memberRepository) {
		this.workspaceRepository = workspaceRepository;
		this.emailUtility = emailUtility;
		this.joinWorkspaceTokenRepository = joinWorkspaceTokenRepository;
		this.userRepository = userRepository;
		this.joinWorkspaceRequestRepository = joinWorkspaceRequestRepository;
		this.memberRepository = memberRepository;
		
	}

	@Transactional
	public WorkspaceResponse createWorkspace(WorkspaceRequest workspaceRequest) {
		Optional<Workspace> workspaceOpt = workspaceRepository.findByWorkspaceName(workspaceRequest.getWorkspaceName());
		if (workspaceOpt.isPresent()) {
			throw new WorkspaceAlreadyPresentException(
					"Workspace with the name " + workspaceRequest.getWorkspaceName() + " is already present");
		}
		Workspace newWorkspace = new Workspace();
		User user = userRepository.findByGmail(workspaceRequest.getAdminGmail()).orElseThrow(() -> new UserNotFoundException("user not found"));
		newWorkspace.setWorkspaceName(workspaceRequest.getWorkspaceName());
		newWorkspace.setOrganizationName(workspaceRequest.getOrganizationName());
		newWorkspace.setWorkspaceType(workspaceRequest.getWorkspaceType());
		newWorkspace.setUsers(workspaceRequest.getUsers());
		newWorkspace.setIsPrivate(workspaceRequest.getIsPrivate());
		newWorkspace.addUser(user);
		newWorkspace.setAdminGmail(workspaceRequest.getAdminGmail());
		String slug = generateWorkspaceSlug(workspaceRequest.getWorkspaceName());
		String uniqueSlug = slug;
		int counter = 1;
		while (workspaceRepository.findByWorkspaceSlug(slug).isPresent()) {
			uniqueSlug = slug + "-" + counter++;
		}
		if(!newWorkspace.getIsPrivate()) {
			newWorkspace.setJoinCode(newWorkspace.generateJoinCode());
		}
		else {
			newWorkspace.setJoinCode(null);
		}
		Member member = new Member();
		newWorkspace.setWorkspaceSlug(uniqueSlug);
		newWorkspace.setWorkspaceRole("ADMIN");
		newWorkspace.setWorkspaceRole(workspaceRequest.getWorkspaceRole());
		newWorkspace.setIsAllowedInvites(workspaceRequest.getIsAllowedInvites());
		member.setMemberName(user.getUsername());
		member.setMemberGmail(user.getGmail());
		member.setMemberJoinDate(LocalDateTime.now());
		newWorkspace.addMember(member);
		memberRepository.save(member);
		workspaceRepository.save(newWorkspace);
		user.addWorkspace(newWorkspace);
		userRepository.save(user);
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

		if(workspaceRequest.getAdminGmail() != null) {
			workspace.setAdminGmail(workspaceRequest.getAdminGmail());
		}
		
		if(workspaceRequest.getIsAllowedInvites() != null) {
			workspace.setIsAllowedInvites(workspaceRequest.getIsAllowedInvites());
		}
		
		if(workspaceRequest.getWorkspaceRole() != null) {
			workspace.setWorkspaceRole(workspaceRequest.getWorkspaceRole());
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
		Set<UserResponse> userResponse = workspace.getUsers().stream().map(user -> {
			UserResponse ur = new UserResponse();
			ur.setGmail(user.getGmail());
			ur.setRoles(user.getRoles());
			ur.setUsername(user.getUsername());
			ur.setVerified(user.isVerified());
			return ur;
		}).collect(Collectors.toSet());
		workspaceResponse.setUsers(userResponse);
		workspaceResponse.setWorkspaceName(workspace.getWorkspaceName());
		workspaceResponse.setWorkspaceType(workspace.getWorkspaceType());
		workspaceResponse.setAdminGmail(workspace.getAdminGmail());
		workspaceResponse.setIsAllowedInvites(workspace.getIsAllowedInvites());
		workspaceResponse.setWorkspaceRole(workspace.getWorkspaceRole());
		workspaceResponse.setWorkspaceType(workspace.getWorkspaceType());
		workspaceResponse.setJoinCode(workspace.getJoinCode());
		workspaceResponse.setIsPrivate(workspace.getIsPrivate());
		Set<JoinWorkspaceRequestResponse> joinWorkspaceRequestResponses = workspace.getRequests().stream().map(request -> {
			JoinWorkspaceRequestResponse jr = new JoinWorkspaceRequestResponse();
			jr.setRequestTime(request.getRequestTime());
			jr.setStatus(request.getStatus());
			return jr;
		}).collect(Collectors.toSet());
		workspaceResponse.setRequests(joinWorkspaceRequestResponses);
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

	@Transactional
	public String joinWorkspace(JoinWorkspaceRequestDto joinWorkspaceRequestDto) {
		Optional<Workspace> workspaceOpt = workspaceRepository.findByWorkspaceSlug(joinWorkspaceRequestDto.getWorkspaceSlug());
		if (workspaceOpt.isEmpty()) {
			throw new WorkspaceNotFoundException("Workspace Not Found with slug " + joinWorkspaceRequestDto.getWorkspaceSlug());
		}
		Workspace workspace = workspaceOpt.get();
		if (!workspace.getIsAllowedInvites()) {
			throw new WorkspaceInviteNotAllowedException("Sorry but the workspace not allow invites");
		}
		
		if(workspace.getIsPrivate()) {
			throw new WorkspaceNotFoundException("Workspace is private.");
		}
		
		User user = userRepository.findByGmail(joinWorkspaceRequestDto.getGmail()).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		if(joinWorkspaceRequestRepository.findByUser(user).isPresent()) {
			throw new JoinWorkspaceRequestAlreadyExistException("Join Workspace Request Already Exists");
		}
		JoinWorkspaceRequest joinWorkspaceRequest = new JoinWorkspaceRequest();
		joinWorkspaceRequest.setRequestTime(LocalDateTime.now());
		joinWorkspaceRequest.setStatus("PENDING");
		joinWorkspaceRequest.setUser(user);
		joinWorkspaceRequest.setWorkspace(workspace);
		joinWorkspaceRequestRepository.save(joinWorkspaceRequest);
		workspace.addRequest(joinWorkspaceRequest);
		workspaceRepository.save(workspace);
		user.addRequest(joinWorkspaceRequest);
		userRepository.save(user);
		return "Request to join Workspace has been sent. Please wait while an admin approve your request!";

	}

	public String createInviteJoinLink(Workspace workspace, String gmail) {
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
		Member member = new Member();
		joinWorkspaceToken.setUsed(true);
		joinWorkspaceToken.setToken(null);
		workspace.addUser(user);
		member.setMemberName(user.getUsername());
		member.setMemberGmail(user.getGmail());
		member.setMemberJoinDate(LocalDateTime.now());
		workspace.addMember(member);
		memberRepository.save(member);
		userRepository.save(user);
		workspaceRepository.save(workspace);
		joinWorkspaceTokenRepository.save(joinWorkspaceToken);
		return "Workspace joined using the invite link";
	}
	
	@Transactional
	public String acceptSingleJoinRequest(Long requestId) {
		JoinWorkspaceRequest joinWorkspaceRequest = joinWorkspaceRequestRepository.findById(requestId).orElseThrow(() -> new JoinWorkspaceRequestNotFound("Workspace Join Request Not Found"));
		joinWorkspaceRequest.setStatus("APPROVED");
		User user = joinWorkspaceRequest.getUser();
		Member member = new Member();
		Workspace workspace = joinWorkspaceRequest.getWorkspace();
		workspace.addUser(user);
		user.addWorkspace(workspace);
		member.setMemberName(user.getUsername());
		member.setMemberGmail(user.getGmail());
		member.setMemberJoinDate(LocalDateTime.now());
		workspace.addMember(member);
		memberRepository.save(member);
		workspaceRepository.save(workspace);
		userRepository.save(user);
		joinWorkspaceRequestRepository.save(joinWorkspaceRequest);
		emailUtility.sendEmail(joinWorkspaceRequest.getUser().getGmail(), "Workspace " + joinWorkspaceRequest.getWorkspace().getWorkspaceName() + " Welcomes You", "Your Request to Join Workspace has been Accepted!");
		return "Your Request to Join Workspace has been Accepted!!";
	}
	
	@Transactional
	public String acceptAllJoinRequest(Long workspaceId) {
		Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));
		Set<JoinWorkspaceRequest> requests = workspace.getRequests();
		for(JoinWorkspaceRequest request : requests) {
			if("PENDING".equals(request.getStatus())) {
				request.setStatus("APPROVED");
				Member member = new Member();
				User user = request.getUser();
				user.addWorkspace(workspace);
				workspace.addUser(user);
				member.setMemberName(user.getUsername());
				member.setMemberGmail(user.getGmail());
				member.setMemberJoinDate(LocalDateTime.now());
				workspace.addMember(member);
				memberRepository.save(member);
				joinWorkspaceRequestRepository.save(request);
				emailUtility.sendEmail(request.getUser().getGmail(), "Workspace " + request.getWorkspace().getWorkspaceName() + " Welcomes You", "Your Request to Join Workspace has been Accepted!");
			}
		}
		workspaceRepository.save(workspace);
		return "All the requests has been Approved";
	}
	
	public String rejectSingleJoinRequest(Long requestId) {
		JoinWorkspaceRequest joinWorkspaceRequest = joinWorkspaceRequestRepository.findById(requestId).orElseThrow(() -> new JoinWorkspaceRequestNotFound("Workspace Join Request Not Found"));
		joinWorkspaceRequest.setStatus("REJECTED");
		joinWorkspaceRequestRepository.save(joinWorkspaceRequest);
		emailUtility.sendEmail(joinWorkspaceRequest.getUser().getGmail(), "Workspace " + joinWorkspaceRequest.getWorkspace().getWorkspaceName() + " Join Request Rejected", "Sorry! But your request to join the workspace has been rejected.");
		return "Your Request to Join Workspace has been Accepted. Workspace join link has been sent to your email!";
	}
	
	public String rejectAllJoinRequest(Long workspaceId) {
		Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));
		Set<JoinWorkspaceRequest> requests = workspace.getRequests();
		for(JoinWorkspaceRequest request : requests) {
			if("PENDING".equals(request.getStatus())) {
				request.setStatus("REJECTED");
				emailUtility.sendEmail(request.getUser().getGmail(), "Workspace " + request.getWorkspace().getWorkspaceName() + " Join Request Rejected", "Sorry! But your request to join the workspace has been rejected.");
				joinWorkspaceRequestRepository.save(request);
			}
		}
		workspaceRepository.save(workspace);
		return "All the requests has been Approved";
	}
	
	public String sendInviteLinkToMembers(String gmail, Workspace workspace) {
		User user = userRepository.findByGmail(gmail).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		String inviteLink = createInviteJoinLink(workspace, gmail);
		emailUtility.sendEmail(user.getGmail(), "You are invited to Join Workspace " + workspace.getWorkspaceName(), "Join the workspace within 2 days from now via:" + inviteLink);
		return "Workspace Invite Link has been sent to the Member";
	}
	
	@Transactional
	public String joinWorkspaceWithJoinCode(JoinWorkspaceRequestDto joinWorkspaceRequestDto) {
		Workspace workspace = workspaceRepository.findByJoinCode(joinWorkspaceRequestDto.getJoinCode()).orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found with the join code"));
		User user = userRepository.findByGmail(joinWorkspaceRequestDto.getGmail()).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		workspace.addUser(user);
		Member member = new Member();
		member.setMemberName(user.getUsername());
		member.setMemberGmail(user.getGmail());
		member.setMemberJoinDate(LocalDateTime.now());
		workspace.addMember(member);
		memberRepository.save(member);
		workspaceRepository.save(workspace);
		user.addWorkspace(workspace);
		userRepository.save(user);
		return "User have joined the workspace";
	}
	
	public Page<WorkspaceSummaryDto> getAllWorkspacesSummaryByUser(int page, int size, String gmail){
		Pageable pageable = PageRequest.of(page, size);
		User user = userRepository.findByGmail(gmail).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		return workspaceRepository.findAllWorkspaceSummariesByUserGmail(gmail, pageable);
		
	}
	
	public Page<WorkspaceSummaryDto> searchWorkspaceByKeyword(int page, int size, String keyword){
		Pageable pageable = PageRequest.of(page, size);
		return workspaceRepository.findAllWorkspaceSummaryByKeyword(keyword, pageable);
	}
	
	public String getWorkspaceJoinCodeByWorkspaceId(Long workspaceId) {
		Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));
		return workspace.getJoinCode();
	}
	
}
