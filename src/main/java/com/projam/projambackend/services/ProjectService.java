package com.projam.projambackend.services;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import com.projam.projambackend.config.RabbitMQConfig;
import com.projam.projambackend.dto.EditProjectRequest;
import com.projam.projambackend.dto.EditProjectResponse;
import com.projam.projambackend.dto.JoinProjectRequestDto;
import com.projam.projambackend.dto.JoinWorkspaceRequestDto;
import com.projam.projambackend.dto.ProjectRequest;
import com.projam.projambackend.dto.ProjectResponse;
import com.projam.projambackend.email.EmailUtility;
import com.projam.projambackend.enums.ProjectStatus;
import com.projam.projambackend.exceptions.JoinProjectTokenAlreadyUsedException;
import com.projam.projambackend.exceptions.JoinProjectTokenExpiredException;
import com.projam.projambackend.exceptions.MemberAlreadyPresentException;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNameAlreadyExistException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.JoinProjectRequest;
import com.projam.projambackend.models.JoinProjectToken;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.MemberRole;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.TaskColumn;
import com.projam.projambackend.models.Workspace;
import com.projam.projambackend.repositories.JoinProjectTokenRepository;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.MemberRoleRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskColumnRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {

	private final ProjectRepository projectRepository;

	private final WorkspaceRepository workspaceRepository;

	private final MemberRepository memberRepository;

	private final MemberRoleRepository memberRoleRepository;

	private final TaskColumnRepository taskColumnRepository;

	private final JoinProjectTokenRepository joinProjectTokenRepository;

	private final EmailUtility emailUtility;

	private final AmqpTemplate amqpTemplate;

	public ProjectService(ProjectRepository projectRepository, WorkspaceRepository workspaceRepository,
			MemberRepository memberRepository, MemberRoleRepository memberRoleRepository,
			TaskColumnRepository taskColumnRepository, JoinProjectTokenRepository joinProjectTokenRepository,
			EmailUtility emailUtility, AmqpTemplate amqpTemplate) {
		this.projectRepository = projectRepository;
		this.workspaceRepository = workspaceRepository;
		this.memberRepository = memberRepository;
		this.memberRoleRepository = memberRoleRepository;
		this.taskColumnRepository = taskColumnRepository;
		this.joinProjectTokenRepository = joinProjectTokenRepository;
		this.emailUtility = emailUtility;
		this.amqpTemplate = amqpTemplate;
	}

	@Transactional
	public ProjectResponse createNewProject(ProjectRequest projectRequest, String workspaceId) {

		if (projectRepository.findByProjectName(projectRequest.getProjectName()).isPresent()) {
			throw new ProjectNameAlreadyExistException(
					"Project with name " + projectRequest.getProjectName() + " already exists");
		}

		Workspace workspace = workspaceRepository.findById(workspaceId)
				.orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));

		Project project = new Project();
		project.setProjectName(projectRequest.getProjectName());
		project.setIsPrivate(projectRequest.getIsPrivate());
		project.setStartDate(projectRequest.getStartDate());
		project.setEndDate(projectRequest.getEndDate());
		project.setStatus(projectRequest.getProjectStatus());
		project.setProjectDescription(projectRequest.getProjectDescription());
		project.setWorkspace(workspace);
		projectRepository.save(project);
		if (projectRequest.getIsPrivate()) {

			Member admin = memberRepository.findByMemberGmailAndWorkspaceId(projectRequest.getAdminGmail(), workspaceId)
					.orElseThrow(() -> new MemberNotFoundException("Admin member not found"));

			project.addMember(admin);
			admin.addProject(project);

			MemberRole adminRole = createMemberRoleWithPermissions(admin, project, "ADMIN");
			adminRole.getMember().add(admin);
			adminRole.setProject(project);
			memberRoleRepository.save(adminRole);

			admin.addMemberRole(adminRole);
			project.getMemberRoles().add(adminRole);
		} else {

			Set<Member> members = memberRepository.findAllByWorkspaceId(workspaceId);
			project.setMembers(members);

			for (Member member : members) {
				member.addProject(project);

				String roleName = member.getMemberGmail().equals(projectRequest.getAdminGmail()) ? "ADMIN" : "MEMBER";
				MemberRole memberRole = createMemberRoleWithPermissions(member, project, roleName);
				memberRole.setProject(project);
				memberRoleRepository.save(memberRole);

				member.addMemberRole(memberRole);
				project.getMemberRoles().add(memberRole);
			}
		}

		List<TaskColumn> defaultColumns = List.of(new TaskColumn("To Do", "bg-bblue", "todo", workspace, project, 1),
				new TaskColumn("In Progress", "bg-yyellow", "inprogress", workspace, project, 2),
				new TaskColumn("Completed", "bg-ggreen", "completed", workspace, project, 3));

		projectRepository.save(project);
		taskColumnRepository.saveAll(defaultColumns);

		return projectToProjectResponse(project);
	}

	private MemberRole createMemberRoleWithPermissions(Member member, Project project, String roleName) {
		MemberRole memberRole = memberRoleRepository
				.findByRoleNameAndProject_ProjectId(roleName, project.getProjectId()).orElse(null);

		if (memberRole == null) {
			memberRole = new MemberRole();
			memberRole.setProject(project);
			memberRole.setRoleName(roleName);

			if ("ADMIN".equals(roleName)) {
				memberRole.setRoleColor("ppurple");
				memberRole.setCanCreateTask(true);
				memberRole.setCanEditTask(true);
				memberRole.setCanDeleteTask(true);
				memberRole.setCanAssignTask(true);
				memberRole.setCanManageMembers(true);
				memberRole.setCanEditProject(true);
				memberRole.setCanDeleteProject(true);
				memberRole.setCanCreateColumn(true);
				memberRole.setCanDeleteColumn(true);
				memberRole.setCanManageRolesAndPermission(true);
				memberRole.setCanManageGithub(true);
			} else if ("MEMBER".equals(roleName)) {
				memberRole.setRoleColor("ggreen");
				memberRole.setCanCreateTask(true);
				memberRole.setCanEditTask(true);
				memberRole.setCanDeleteTask(false);
				memberRole.setCanAssignTask(false);
				memberRole.setCanManageMembers(false);
				memberRole.setCanEditProject(false);
				memberRole.setCanDeleteProject(false);
				memberRole.setCanCreateColumn(false);
				memberRole.setCanDeleteColumn(false);
				memberRole.setCanManageRolesAndPermission(false);
				memberRole.setCanManageGithub(false);
			}

			memberRole = memberRoleRepository.save(memberRole);
		}

		memberRole.getMember().add(member);

		return memberRole;
	}

	public Page<ProjectResponse> getAllProjectsByWorkspaceAndEmail(int size, int page, String workspaceId,
			String email) {
		Pageable pageable = PageRequest.of(page, size);
		return projectRepository.findAllProjectResponseByWorkspaceAndEmail(workspaceId, email, pageable);
	}

	public ProjectResponse projectToProjectResponse(Project project) {
		ProjectResponse projectResponse = new ProjectResponse();
		projectResponse.setProjectId(project.getProjectId());
		projectResponse.setProjectName(project.getProjectName());
		projectResponse.setIsPrivate(project.getIsPrivate());
		projectResponse.setStartDate(project.getStartDate());
		projectResponse.setEndDate(project.getEndDate());
		projectResponse.setProjectStatus(project.getStatus());
		return projectResponse;
	}

	public String sendProjectJoinRequest(JoinProjectRequestDto joinProjectRequestDto) {
		Workspace workspace = workspaceRepository.findById(joinProjectRequestDto.getWorkspaceId())
				.orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));
		Project project = projectRepository.findById(joinProjectRequestDto.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		String joinProjectLink = createInviteJoinLink(workspace, project, joinProjectRequestDto.getEmail());
		emailUtility.sendEmail(joinProjectRequestDto.getEmail(), "You have been invited to Join a Project",
				"Join the project in 2 days with the link " + joinProjectLink);
		return "Email has been sent to Join Project";

	}

	public String createInviteJoinLink(Workspace workspace, Project project, String gmail) {
		String token = UUID.randomUUID().toString();
		JoinProjectToken joinToken = new JoinProjectToken();
		joinToken.setToken(token);
		joinToken.setWorkspace(workspace);
		joinToken.setProject(project);
		joinToken.setExpiresAt(LocalDateTime.now().plusDays(2));
		joinToken.setUsed(false);
		joinToken.setEmail(gmail);
		joinProjectTokenRepository.save(joinToken);

		return "http://localhost:8080/projam/project/join/project/" + token;
	}

	public RedirectView joinProjectWithInviteLink(String token) {
		JoinProjectToken joinProjectToken = joinProjectTokenRepository.findByToken(token)
				.orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		if (joinProjectToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new JoinProjectTokenExpiredException("Join Project Request is Expired");
		}
		if (joinProjectToken.getUsed()) {
			throw new JoinProjectTokenAlreadyUsedException("Join Project Token Already Used");
		}
		Member member = memberRepository
				.findByMemberGmailAndWorkspaceId(joinProjectToken.getEmail(),
						joinProjectToken.getWorkspace().getWorkspaceId())
				.orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
		Project project = joinProjectToken.getProject();
		boolean isAlreadyPresent = project.getMembers().stream()
				.anyMatch(projectMember -> projectMember.getMemberGmail().equals(member.getMemberGmail()));
		if (isAlreadyPresent) {
			throw new MemberAlreadyPresentException("Member Already Present in Project");
		}
		project.addMember(member);
		member.addProject(project);
		MemberRole memberRole = createMemberRoleWithPermissions(member, project, "MEMBER");
		memberRoleRepository.save(memberRole);
		member.addMemberRole(memberRole);
		joinProjectToken.setUsed(true);
		project.getJoinToken().add(joinProjectToken);
		joinProjectTokenRepository.save(joinProjectToken);
		projectRepository.save(project);
		memberRepository.save(member);

		return new RedirectView("http://localhost:5173/home/workspaces");

	}

	public EditProjectResponse updateProject(EditProjectRequest editProjectRequest) {
		Project project = projectRepository.findById(editProjectRequest.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException("project Not Found"));
		project.setProjectName(editProjectRequest.getProjectName());
		project.setProjectDescription(editProjectRequest.getProjectDescription());
		projectRepository.save(project);
		return new EditProjectResponse(project.getProjectName(), project.getProjectDescription());
	}

	public EditProjectResponse getEditProjectResponseByProjectId(String projectId) {
		EditProjectResponse editProjectResponse = projectRepository.findProjectResponseByProjectId(projectId)
				.orElseThrow(() -> new RuntimeException("Project Response Not Found"));
		return editProjectResponse;
	}

	public String deleteProject(String projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		project.setProjectStatus(ProjectStatus.DELETION_PENDING);
		project.setDeletionMarkedAt(new Date());
		projectRepository.save(project);
		return "Project Deletion Task is Queued";
	}

	public void deleteProjectSchedule() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -15);
		Date thresholdDate = calendar.getTime();
		List<String> projectIds = projectRepository.findAllByProjectStatusAndThresholdTime(ProjectStatus.DELETION_PENDING, thresholdDate);
		projectIds.forEach(projectId -> amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,
				RabbitMQConfig.ROUTING_KEY, projectId));
	}
	
	public List<ProjectResponse> getAllProjectMarkedAsDeleted(String workspaceId){
		List<ProjectResponse> projectResponses = projectRepository.findAllByProjectStatusAndWorkspaceId(ProjectStatus.DELETION_PENDING, workspaceId);
		return projectResponses;
	}
	
	public String restoreProject(String projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		if(project.getTasks().size() >= 1) {
			project.setProjectStatus(ProjectStatus.IN_PROGRESS);
		}
		else {
			project.setProjectStatus(ProjectStatus.NOT_STARTED);
		}
		project.setDeletionMarkedAt(null);
		projectRepository.save(project);
		return "Project Restoration Task is Queued";
	}

}
