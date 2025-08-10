package com.projam.projambackend.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.TagRequest;
import com.projam.projambackend.dto.TagResponse;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.models.MemberRole;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.Tag;
import com.projam.projambackend.repositories.MemberRoleRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TagRepository;

@Service
public class TagService {

	private final TagRepository tagRepository;
	
	private final ProjectRepository projectRepository;
	
	private final MemberRoleRepository memberRoleRepository;
	
	public TagService(TagRepository tagRepository, ProjectRepository projectRepository, MemberRoleRepository memberRoleRepository) {
		this.tagRepository = tagRepository;
		this.projectRepository = projectRepository;
		this.memberRoleRepository = memberRoleRepository;
	}
	
	public List<TagResponse> getAllTagsByProjectId(String projectId){
		return tagRepository.findAllByProjectId(projectId);
	}
	
	public String createTag(TagRequest tagRequest, String projectId) {
		Tag tag = new Tag();
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		tag.setProject(project);
		tag.setTitle(tagRequest.getTitle().toLowerCase().replace(" ", ""));
		List<MemberRole> roles = tagRequest.getMemberRoleId().stream()
			        .map(id -> memberRoleRepository.findById(id)
			            .orElseThrow(() -> new RuntimeException("Role not found")))
			        .collect(Collectors.toList());
		tag.setMemberRole(roles);
		project.getTags().add(tag);
		projectRepository.save(project);
		tagRepository.save(tag);
		return "Tag Created Successfully";
		
	}
	
	public List<String> suggestTags(String projectId, String query){
		String queryParam = query + "%";
		return tagRepository.findAllTagNameByProjectIdAndQuery(projectId, queryParam);
		
	}
	
	public List<String> suggestTags(String projectId){
		return tagRepository.findAllTagNameByProjectId(projectId);
		
	}
}
