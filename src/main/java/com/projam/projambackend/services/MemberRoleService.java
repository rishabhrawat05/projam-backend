package com.projam.projambackend.services;

import java.util.List;

import org.springframework.stereotype.Service;


import com.projam.projambackend.dto.MemberRoleRequest;
import com.projam.projambackend.dto.MemberRoleResponse;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.MemberRole;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.MemberRoleRepository;
import com.projam.projambackend.repositories.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class MemberRoleService {

	private final MemberRoleRepository memberRoleRepository;
	private final MemberRepository memberRepository;
	private final ProjectRepository projectRepository;
	
	public MemberRoleService(MemberRoleRepository memberRoleRepository, MemberRepository memberRepository, ProjectRepository projectRepository) {
		this.memberRoleRepository = memberRoleRepository;
		this.memberRepository = memberRepository;
		this.projectRepository = projectRepository;
	}
	
	public List<MemberRoleResponse> getAllMemberRoleByProjectId(String projectId){
		return memberRoleRepository.findAllByProject_ProjectId(projectId);
	}
	
	@Transactional
	public MemberRoleResponse createNewMemberRole(MemberRoleRequest memberRoleRequest, String projectId) {
		MemberRole memberRole = new MemberRole();
		memberRole.setCanAssignTask(memberRoleRequest.isCanAssignTask());
		memberRole.setCanCreateColumn(memberRoleRequest.isCanCreateColumn());
		memberRole.setCanCreateTask(memberRoleRequest.isCanCreateTask());
		memberRole.setCanDeleteColumn(memberRoleRequest.isCanDeleteColumn());
		memberRole.setCanDeleteProject(memberRoleRequest.isCanDeleteProject());
		memberRole.setCanDeleteTask(memberRoleRequest.isCanDeleteTask());
		memberRole.setCanEditProject(memberRoleRequest.isCanEditProject());
		memberRole.setCanEditTask(memberRoleRequest.isCanEditTask());
		memberRole.setCanManageMembers(memberRoleRequest.isCanManageMembers());
		memberRole.setCanManageRolesAndPermission(memberRoleRequest.isCanManageRolesAndPermission());
		memberRole.setCanManageGithub(memberRoleRequest.isCanManageGithub());
		
		
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		
		memberRole.setProject(project);
		memberRole.setRoleColor(memberRoleRequest.getRoleColor());
		memberRole.setRoleName(memberRoleRequest.getRoleName());
		memberRoleRepository.save(memberRole);
		memberRoleRequest.getMembers().forEach(member -> {
		    Member foundMember = memberRepository
		        .findByMemberGmailAndProjectId(member.getMemberGmail(), projectId)
		        .orElseThrow(() -> new MemberNotFoundException("Member Not Found"));

		    memberRole.addMember(foundMember);
		    foundMember.addMemberRole(memberRole);
		    memberRepository.save(foundMember);
		});
		
		memberRoleRepository.save(memberRole);
		
		return memberRoleToMemberRoleResponse(memberRole);
		
	}
	
	private MemberRoleResponse memberRoleToMemberRoleResponse(MemberRole memberRole) {
		MemberRoleResponse memberRoleResponse = new MemberRoleResponse();
		memberRoleResponse.setCanAssignTask(memberRole.isCanAssignTask());
		memberRoleResponse.setCanCreateColumn(memberRole.isCanCreateColumn());
		memberRoleResponse.setCanCreateTask(memberRole.isCanCreateTask());
		memberRoleResponse.setCanDeleteColumn(memberRole.isCanDeleteColumn());
		memberRoleResponse.setCanDeleteProject(memberRole.isCanDeleteProject());
		memberRoleResponse.setCanDeleteTask(memberRole.isCanDeleteTask());
		memberRoleResponse.setCanEditProject(memberRole.isCanEditProject());
		memberRoleResponse.setCanEditTask(memberRole.isCanEditTask());
		memberRoleResponse.setCanManageMembers(memberRole.isCanManageMembers());
		memberRoleResponse.setMemberRoleId(memberRole.getMemberRoleId());
		memberRoleResponse.setMemberRoleName(memberRole.getRoleName());
		memberRoleResponse.setRoleColor(memberRole.getRoleColor());
		memberRoleResponse.setCanManageRolesAndPermission(memberRole.isCanManageRolesAndPermission());
		memberRoleResponse.setCanManageGithub(memberRole.isCanManageGithub());
		return memberRoleResponse;
		
	}
	
	public List<MemberRoleResponse> getMemberRoleByProjectIdAndMemberGmail(String memberGmail, String projectId) {
		return memberRoleRepository.getMemberRoleByProjectIdAndMemberGmail(projectId, memberGmail);
	}
	
	
}
