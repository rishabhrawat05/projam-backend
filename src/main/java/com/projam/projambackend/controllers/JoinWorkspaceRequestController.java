package com.projam.projambackend.controllers;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.projam.projambackend.dto.JoinWorkspaceRequestResponseSummary;
import com.projam.projambackend.services.JoinWorkspaceRequestService;

@RestController
@RequestMapping("/projam/join-workspace-request")
public class JoinWorkspaceRequestController {

	private final JoinWorkspaceRequestService joinWorkspaceRequestService;
	
	public JoinWorkspaceRequestController(JoinWorkspaceRequestService joinWorkspaceRequestService) {
		this.joinWorkspaceRequestService = joinWorkspaceRequestService;
	}
	
	@GetMapping("/get-all")
	public List<JoinWorkspaceRequestResponseSummary> getAllRequestByWorkspace(@RequestParam String workspaceId){
		return joinWorkspaceRequestService.getAllJoinWorkspaceRequests(workspaceId);
	}
}
