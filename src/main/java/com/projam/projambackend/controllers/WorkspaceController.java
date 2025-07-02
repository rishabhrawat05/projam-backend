package com.projam.projambackend.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.projam.projambackend.dto.JoinWorkspaceRequestDto;
import com.projam.projambackend.dto.WorkspaceRequest;
import com.projam.projambackend.services.WorkspaceService;

@RestController
@RequestMapping("/projam/workspace")
public class WorkspaceController {

	private final WorkspaceService workspaceService;

	public WorkspaceController(WorkspaceService workspaceService) {
		this.workspaceService = workspaceService;
	}

	@PostMapping("/create/workspace")
	public ResponseEntity<?> createWorkspace(@RequestBody WorkspaceRequest workspaceRequest) {
		return ResponseEntity.ok(workspaceService.createWorkspace(workspaceRequest));
	}

	@PutMapping("/update/workspace")
	public ResponseEntity<?> updateWorkspace(@RequestBody WorkspaceRequest workspaceRequest,
			@RequestParam String workspaceId) {
		return ResponseEntity.ok(workspaceService.updateWorkspace(workspaceRequest, workspaceId));
	}

	@DeleteMapping("/delete/workspace")
	public ResponseEntity<?> deleteWorkspace(@RequestParam String workspaceId) {
		return ResponseEntity.ok(workspaceService.deleteWorkspaceById(workspaceId));
	}

	@GetMapping("/allworkspaces")
	public ResponseEntity<?> getAllWorkspaces() {
		return ResponseEntity.ok(workspaceService.getAllWorkspaces());
	}

	@PreAuthorize(value = "hasRole('ADMIN')")
	@GetMapping("/workspace")
	public ResponseEntity<?> getWorkspaceById(@RequestParam String workspaceId) {
		return ResponseEntity.ok(workspaceService.getWorkspaceById(workspaceId));
	}

	@PostMapping("/join/workspace/slug")
	public ResponseEntity<?> joinWorkspaceWithSlug(@RequestBody JoinWorkspaceRequestDto joinWorkspaceRequestDto) {
		return ResponseEntity.ok(workspaceService.joinWorkspace(joinWorkspaceRequestDto));
	}

	@GetMapping("/join/workspace/{token}")
	public RedirectView joinWorkspaceWithInviteLink(@PathVariable String token) {
		return workspaceService.joinWorkspaceWithInviteLink(token);
	}

	@GetMapping("/accept/request")
	public ResponseEntity<?> acceptSingleJoinRequest(@RequestParam String requestId) {
		return ResponseEntity.ok(workspaceService.acceptSingleJoinRequest(requestId));
	}

	@GetMapping("/accept/all/request")
	public ResponseEntity<?> acceptAllJoinRequest(@RequestParam String workspaceId) {
		return ResponseEntity.ok(workspaceService.acceptAllJoinRequest(workspaceId));
	}

	@PostMapping("/join/workspace/joincode")
	public ResponseEntity<?> joinWorkspaceWithJoinCode(@RequestBody JoinWorkspaceRequestDto joinWorkspaceRequestDto) {
		return ResponseEntity.ok(workspaceService.joinWorkspaceWithJoinCode(joinWorkspaceRequestDto));
	}

	@GetMapping("/all/workspaces")
	public Page<?> getAllWorkspacesByUser(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, 
			@RequestParam String gmail) {
		return workspaceService.getAllWorkspacesSummaryByUser(page, size, gmail);

	}
	
	@GetMapping("/search")
	public Page<?> searchWorkspaceByKeyword(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, 
			@RequestParam String keyword){
		return workspaceService.searchWorkspaceByKeyword(page,size,keyword);
	}
	
	@GetMapping("/get/joincode")
	public ResponseEntity<?> getWorkspaceJoinCodeByWorkspaceId(@RequestParam String workspaceId){
		return ResponseEntity.ok(workspaceService.getWorkspaceJoinCodeByWorkspaceId(workspaceId));
	}
}
