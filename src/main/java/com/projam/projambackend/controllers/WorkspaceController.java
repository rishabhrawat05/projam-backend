package com.projam.projambackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.JoinWorkspaceRequest;
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
	public ResponseEntity<?> createWorkspace(@RequestBody WorkspaceRequest workspaceRequest){
		return ResponseEntity.ok(workspaceService.createWorkspace(workspaceRequest));
	}
	
	@PutMapping("/update/workspace")
	public ResponseEntity<?> updateWorkspace(@RequestBody WorkspaceRequest workspaceRequest, @RequestParam Long workspaceId){
		return ResponseEntity.ok(workspaceService.updateWorkspace(workspaceRequest, workspaceId));
	}
	
	@DeleteMapping("/delete/workspace")
	public ResponseEntity<?> deleteWorkspace(@RequestParam Long workspaceId){
		return ResponseEntity.ok(workspaceService.deleteWorkspaceById(workspaceId));
	}
	
	@GetMapping("/allworkspaces")
	public ResponseEntity<?> getAllWorkspaces(){
		return ResponseEntity.ok(workspaceService.getAllWorkspaces());
	}
	
	@GetMapping("/workspace")
	public ResponseEntity<?> getWorkspaceById(@RequestParam Long workspaceId){
		return ResponseEntity.ok(workspaceService.getWorkspaceById(workspaceId));
	}
	
	@PostMapping("/join/workspace")
	public ResponseEntity<?> joinWorkspace(@RequestBody JoinWorkspaceRequest joinWorkspaceRequest){
		return ResponseEntity.ok(workspaceService.joinWorkspace(joinWorkspaceRequest));
	}
	
	@GetMapping("/join/workspace/{token}")
	public ResponseEntity<?> joinWorkspaceWithInviteLink(@PathVariable String token){
		return ResponseEntity.ok(workspaceService.joinWorkspaceWithInviteLink(token));
	}
}
