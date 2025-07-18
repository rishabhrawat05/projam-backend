package com.projam.projambackend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.GithubAutomationRequest;
import com.projam.projambackend.dto.GithubAutomationResponse;
import com.projam.projambackend.exceptions.GithubAutomationNotFoundException;
import com.projam.projambackend.models.GithubAutomation;
import com.projam.projambackend.repositories.GithubAutomationRepository;

@Service
public class GithubAutomationService {

	private final GithubAutomationRepository githubAutomationRepository;
	
	public GithubAutomationService(GithubAutomationRepository githubAutomationRepository) {
		this.githubAutomationRepository = githubAutomationRepository;
	}
	
	public List<GithubAutomationResponse> getAllGithubAutomationByProjectId(String projectId){
		return githubAutomationRepository.getAllGithubAutomationResponseByProjectId(projectId);
	}
	
	public String updateGithubAutomation(GithubAutomationRequest githubAutomationRequest) {
		GithubAutomation githubAutomation = githubAutomationRepository.findById(githubAutomationRequest.getEdgeId()).orElseThrow(() -> new GithubAutomationNotFoundException("Github Automation Not Found"));
		githubAutomation.setSourceEvent(githubAutomationRequest.getSourceEvent());
		githubAutomation.setTargetColumn(githubAutomationRequest.getTargetColumn());
		githubAutomation.setColor(githubAutomationRequest.getColor());
		githubAutomationRepository.save(githubAutomation);
		return "Github Automation Updated Succesfully";
	}
}
