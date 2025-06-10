package com.projam.projambackend.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<?> createNewProject(@RequestBody ProjectRequest projectRequest, @RequestParam Long workspaceId){
		return ResponseEntity.ok(projectService.createNewProject(projectRequest, workspaceId));
	}
	
	@GetMapping("/get")
	public Page<ProjectResponse> getAllProjectByWorkspace(@RequestParam Long workspaceId, @RequestParam int size, @RequestParam int page){
		return projectService.getAllProjectsByWorkspace(size, page, workspaceId);
	}
	
}
