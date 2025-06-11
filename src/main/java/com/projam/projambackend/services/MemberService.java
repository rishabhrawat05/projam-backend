package com.projam.projambackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.MemberRequest;
import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;

import com.projam.projambackend.models.Workspace;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	private final WorkspaceService workspaceService;

	private final WorkspaceRepository workspaceRepository;

	public MemberService(MemberRepository memberRepository, WorkspaceService workspaceService,
			WorkspaceRepository workspaceRepository) {
		this.memberRepository = memberRepository;
		this.workspaceService = workspaceService;
		this.workspaceRepository = workspaceRepository;
	}

	public String addMemberToWorkspace(MemberRequest memberRequest) {
		Workspace workspace = workspaceRepository.findById(memberRequest.getWorkspaceId())
				.orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found"));
		return workspaceService.sendInviteLinkToMembers(memberRequest.getMemberGmail(), workspace);
		
	}

	public Page<MemberResponse> getAllMembersByWorkspaceId(Long workspaceId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return memberRepository.findAllMemberByWorkspaceId(workspaceId, pageable);
	}

}
