package com.projam.projambackend.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.GithubAutomationRequest;
import com.projam.projambackend.dto.GithubAutomationResponse;
import com.projam.projambackend.services.GithubAutomationService;

@RestController
@RequestMapping("/projam/github-automation")
public class GithubAutomationController {

	private final GithubAutomationService githubAutomationService;
	
	public GithubAutomationController(GithubAutomationService githubAutomationService) {
		this.githubAutomationService = githubAutomationService;
	}
	
	@GetMapping("/get-all")
	public List<GithubAutomationResponse> getAllGithubAutomationByProjectId(@RequestParam String projectId){
		return githubAutomationService.getAllGithubAutomationByProjectId(projectId);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateGithubAutomation(@RequestBody GithubAutomationRequest githubAutomationRequest){
		return ResponseEntity.ok(githubAutomationService.updateGithubAutomation(githubAutomationRequest));
	}
}
