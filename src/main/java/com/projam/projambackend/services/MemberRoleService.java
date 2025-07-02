package com.projam.projambackend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.MemberRoleResponse;
import com.projam.projambackend.repositories.MemberRoleRepository;

@Service
public class MemberRoleService {

	private final MemberRoleRepository memberRoleRepository;
	
	public MemberRoleService(MemberRoleRepository memberRoleRepository) {
		this.memberRoleRepository = memberRoleRepository;
	}
	
	public List<MemberRoleResponse> getAllMemberRoleByProjectId(String projectId){
		return memberRoleRepository.findAllByProject_ProjectId(projectId);
	}
}
