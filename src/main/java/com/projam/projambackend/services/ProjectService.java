package com.projam.projambackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.ProjectRequest;
import com.projam.projambackend.dto.ProjectResponse;
import com.projam.projambackend.exceptions.ProjectNameAlreadyExistException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.exceptions.WorkspaceNotFoundException;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.WorkspaceRepository;

@Service
public class ProjectService {

	private final ProjectRepository projectRepository;
	
	private final WorkspaceRepository workspaceRepository;
	
	public ProjectService(ProjectRepository projectRepository, WorkspaceRepository workspaceRepository) {
		this.projectRepository = projectRepository;
		this.workspaceRepository = workspaceRepository;
	}
	
	public ProjectResponse createNewProject(ProjectRequest projectRequest, Long workspaceId) {
		if(projectRepository.findByProjectName(projectRequest.getProjectName()).isPresent()) {
			throw new ProjectNameAlreadyExistException("Project with name " + projectRequest.getProjectName() + " already exists");
		}
		Project project = new Project();
		project.setProjectName(projectRequest.getProjectName());
		project.setIsPrivate(projectRequest.getIsPrivate());
		project.setStartDate(projectRequest.getStartDate());
		project.setEndDate(projectRequest.getEndDate());
		project.setStatus(projectRequest.getProjectStatus());
		project.setProjectDescription(projectRequest.getProjectDescription());
		project.setWorkspace(workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace Not Found")));
		projectRepository.save(project);
		return projectToProjectResponse(project);
	}
	
	public String deleteProject(Long projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		projectRepository.delete(project);
		return "Project has been deleted successfully";
	}
	
	public Page<ProjectResponse> getAllProjectsByWorkspace(int size, int page, Long workspaceId){
		Pageable pageable = PageRequest.of(page, size);
		return projectRepository.findAllProjectResponseByWorkspace(workspaceId, pageable);
	}
	
	public ProjectResponse projectToProjectResponse(Project project) {
		ProjectResponse projectResponse = new ProjectResponse();
		projectResponse.setProjectName(project.getProjectName());
		projectResponse.setIsPrivate(project.getIsPrivate());
		projectResponse.setStartDate(project.getStartDate());
		projectResponse.setEndDate(project.getEndDate());
		projectResponse.setProjectStatus(project.getStatus());
		return projectResponse;
	}
	
	
	
}
