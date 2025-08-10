package com.projam.projambackend.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.MemberRequest;
import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.dto.MemberSummary;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.Workspace;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	private final WorkspaceService workspaceService;

	private final WorkspaceRepository workspaceRepository;
	
	private final ProjectRepository projectRepository;

	public MemberService(MemberRepository memberRepository, WorkspaceService workspaceService,
			WorkspaceRepository workspaceRepository, ProjectRepository projectRepository) {
		this.memberRepository = memberRepository;
		this.workspaceService = workspaceService;
		this.workspaceRepository = workspaceRepository;
		this.projectRepository = projectRepository;
	}

	public String addMemberToWorkspace(MemberRequest memberRequest) {
		Workspace workspace = workspaceRepository.findById(memberRequest.getWorkspaceId())
				.orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));
		return workspaceService.sendInviteLinkToMembers(memberRequest.getMemberGmail(), workspace);
		
	}

	public Page<MemberResponse> getAllMembersByWorkspaceId(String workspaceId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return memberRepository.findAllMemberByWorkspaceId(workspaceId, pageable);
	}
	
	public Page<MemberResponse> getAllMembersByProjectId(String projectId, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return memberRepository.findAllMemberByProjectId(projectId, pageable);
	}

	public List<MemberResponse> getAllMembersByKeyword(String keyword, String projectId){
		return memberRepository.getAllMembersByKeyword(keyword, projectId);
	}
	
	public String removeMemberFromProject(String memberId, String projectId) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		project.getMembers().remove(member);
		member.getProjects().remove(project);
		projectRepository.save(project);
		memberRepository.save(member);
		return "Member Removed From Project";
	}
	
	public List<String> suggestMemberName(String query, String projectId){
		String queryParam = query + "%";
		return memberRepository.findAllMemberNameByProjectIdAndQuery(projectId, queryParam);
	}
	
	public List<String> suggestMemberName(String projectId){
		return memberRepository.findAllMemberNameByProjectId(projectId);
	}
	
	public List<MemberSummary> findAllMemberNameByWorkspaceId(String workspaceId, String projectId){
		return memberRepository.findAllMemberNameByWorkspaceId(workspaceId, projectId);
	}
	
	public List<MemberResponse> getAllMembersByKeywordAndWorkspaceId(String keyword, String workspaceId){
		return memberRepository.getAllMembersByKeywordAndWorkspaceId(keyword, workspaceId);
	}
	
}
