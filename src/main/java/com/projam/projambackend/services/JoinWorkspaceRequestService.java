package com.projam.projambackend.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.projam.projambackend.dto.JoinWorkspaceRequestResponseSummary;
import com.projam.projambackend.repositories.JoinWorkspaceRequestRepository;

@Service
public class JoinWorkspaceRequestService {

	private final JoinWorkspaceRequestRepository joinWorkspaceRequestRepository;
	
	public JoinWorkspaceRequestService(JoinWorkspaceRequestRepository joinWorkspaceRequestRepository) {
		this.joinWorkspaceRequestRepository = joinWorkspaceRequestRepository;
	}
	
	public List<JoinWorkspaceRequestResponseSummary> getAllJoinWorkspaceRequests(String workspaceId){
		return joinWorkspaceRequestRepository.getAllRequestByWorkspaceIdAndPendingStatus(workspaceId);
	}
}
