package com.projam.projambackend.services;



import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.ProjectRequest;
import com.projam.projambackend.dto.ProjectResponse;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNameAlreadyExistException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.MemberRole;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.TaskColumn;
import com.projam.projambackend.models.Workspace;
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
	
	public ProjectService(ProjectRepository projectRepository, WorkspaceRepository workspaceRepository, MemberRepository memberRepository, MemberRoleRepository memberRoleRepository, TaskColumnRepository taskColumnRepository) {
		this.projectRepository = projectRepository;
		this.workspaceRepository = workspaceRepository;
		this.memberRepository = memberRepository;
		this.memberRoleRepository = memberRoleRepository;
		this.taskColumnRepository = taskColumnRepository;
	}
	
	@Transactional
	public ProjectResponse createNewProject(ProjectRequest projectRequest, String workspaceId) {

	    if (projectRepository.findByProjectName(projectRequest.getProjectName()).isPresent()) {
	        throw new ProjectNameAlreadyExistException("Project with name " + projectRequest.getProjectName() + " already exists");
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

	    if (projectRequest.getIsPrivate()) {

	        Member admin = memberRepository.findByMemberGmailAndWorkspaceId(projectRequest.getAdminGmail(), workspaceId)
	                .orElseThrow(() -> new MemberNotFoundException("Admin member not found"));

	        project.addMember(admin);
	        admin.addProject(project);

	        MemberRole adminRole = createMemberRoleWithPermissions(admin, project, "ADMIN");
	        admin.addMemberRole(adminRole);

	    } else {

	        Set<Member> members = memberRepository.findAllByWorkspaceId(workspaceId);
	        project.setMembers(members);

	        for (Member member : members) {
	            member.addProject(project);

	            String roleName = member.getMemberGmail().equals(projectRequest.getAdminGmail()) ? "ADMIN" : "MEMBER";
	            MemberRole memberRole = createMemberRoleWithPermissions(member, project, roleName);
	            member.addMemberRole(memberRole);
	        }
	    }

	    List<TaskColumn> defaultColumns = List.of(
	            new TaskColumn("To Do", "bg-bblue", "todo", workspace, project, 1),
	            new TaskColumn("In Progress", "bg-yyellow", "inprogress", workspace, project, 2),
	            new TaskColumn("Completed", "bg-ggreen", "completed", workspace, project, 3)
	    );

	    projectRepository.save(project);
	    taskColumnRepository.saveAll(defaultColumns);

	    return projectToProjectResponse(project);
	}

	
	private MemberRole createMemberRoleWithPermissions(Member member, Project project, String roleName) {
	    MemberRole memberRole = new MemberRole();
	    memberRole.setMember(member);
	    memberRole.setProject(project);
	    memberRole.setRoleName(roleName);

	    if ("ADMIN".equals(roleName)) {
	        memberRole.setCanCreateTask(true);
	        memberRole.setCanEditTask(true);
	        memberRole.setCanDeleteTask(true);
	        memberRole.setCanAssignTask(true);
	        memberRole.setCanManageMembers(true);
	        memberRole.setCanEditProject(true);
	        memberRole.setCanDeleteProject(true);
	        memberRole.setCanCreateColumn(true);
	        memberRole.setCanDeleteColumn(true);
	    } else if ("MEMBER".equals(roleName)) {
	        memberRole.setCanCreateTask(true);
	        memberRole.setCanEditTask(true);
	        memberRole.setCanDeleteTask(false);
	        memberRole.setCanAssignTask(false);
	        memberRole.setCanManageMembers(false);
	        memberRole.setCanEditProject(false);
	        memberRole.setCanDeleteProject(false);
	        memberRole.setCanCreateColumn(false);
	        memberRole.setCanDeleteColumn(false);
	    }

	    return memberRole;
	}

	
	public String deleteProject(String projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		projectRepository.delete(project);
		return "Project has been deleted successfully";
	}
	
	public Page<ProjectResponse> getAllProjectsByWorkspace(int size, int page, String workspaceId){
		Pageable pageable = PageRequest.of(page, size);
		return projectRepository.findAllProjectResponseByWorkspace(workspaceId, pageable);
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
	
	
}
