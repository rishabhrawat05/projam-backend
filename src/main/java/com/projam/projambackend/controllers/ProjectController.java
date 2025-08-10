package com.projam.projambackend.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.view.RedirectView;

import com.projam.projambackend.dto.EditProjectRequest;
import com.projam.projambackend.dto.JoinProjectRequestDto;
import com.projam.projambackend.dto.ProjectRequest;
import com.projam.projambackend.dto.ProjectResponse;
import com.projam.projambackend.services.ProjectService;

@RestController
@RequestMapping("/projam/project")
public class ProjectController {

	private final ProjectService projectService;
	
	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createNewProject(@RequestBody ProjectRequest projectRequest, @RequestParam String workspaceId){
		return ResponseEntity.ok(projectService.createNewProject(projectRequest, workspaceId));
	}
	
	@GetMapping("/get")
	public Page<ProjectResponse> getAllProjectByWorkspaceAndEmail(@RequestParam String workspaceId, @RequestParam String email, @RequestParam int size, @RequestParam int page){
		return projectService.getAllProjectsByWorkspaceAndEmail(size, page, workspaceId, email);
	}
	
	@PostMapping("/send-request")
	public ResponseEntity<?> sendJoinProjectRequest(@RequestBody JoinProjectRequestDto joinProjectRequestDto){
		return ResponseEntity.ok(projectService.sendProjectJoinRequest(joinProjectRequestDto));
	}
	
	@GetMapping("/join/project/{token}")
	public RedirectView joinProjectWithInviteLink(@PathVariable String token) {
		return projectService.joinProjectWithInviteLink(token);
	}
	
	@PutMapping("/update-project")
	public ResponseEntity<?> updateProject(@RequestBody EditProjectRequest editProjectRequest){
		return ResponseEntity.ok(projectService.updateProject(editProjectRequest));
	}
	
	@GetMapping("/get/edit-project-response")
	public ResponseEntity<?> getEditProjectResponseByProjectId(@RequestParam String projectId){
		return ResponseEntity.ok(projectService.getEditProjectResponseByProjectId(projectId));
	}
	
	@PutMapping("/delete-project")
	public ResponseEntity<?> deleteProjectByProjectId(@RequestParam String projectId){
		return ResponseEntity.ok(projectService.deleteProject(projectId));
	}
	
	@GetMapping("/get-all/deletion-pending")
	public List<ProjectResponse> getAllProjectMarkedAsDeleted(@RequestParam String workspaceId){
		return projectService.getAllProjectMarkedAsDeleted(workspaceId);
	}
	
	@PutMapping("/restore-project")
	public ResponseEntity<?> restoreProjectByProjectId(@RequestParam String projectId){
		return ResponseEntity.ok(projectService.restoreProject(projectId));
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ProjectResponse>> getProjectByProjectNameAndWorkspaceIdAndEmail(
			@RequestParam String workspaceId,
			@RequestParam String email,
			@RequestParam String keyword
			){
		return ResponseEntity.ok(projectService.getProjectByProjectNamByWorkspaceIdAndGmail(workspaceId, email, keyword));
	}
}
