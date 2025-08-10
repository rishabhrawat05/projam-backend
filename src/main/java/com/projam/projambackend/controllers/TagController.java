package com.projam.projambackend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.TagRequest;
import com.projam.projambackend.dto.TagResponse;
import com.projam.projambackend.services.TagService;

@RestController
@RequestMapping("/projam/tag")
public class TagController {

	private final TagService tagService;
	
	public TagController(TagService tagService) {
		this.tagService =  tagService;
	}
	
	@GetMapping("/get-all")
	public List<TagResponse> getAllTagsByProjectId(@RequestParam String projectId){
		return tagService.getAllTagsByProjectId(projectId);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createNewTag(@RequestBody TagRequest tagRequest, @RequestParam String projectId){
		return ResponseEntity.ok(tagService.createTag(tagRequest, projectId));
	}
	
	@GetMapping("/suggest")
	public ResponseEntity<List<String>> suggestTags(
			@RequestParam String projectId,
			@RequestParam(required = false) String query
			){
		if(query == null) {
			return ResponseEntity.ok(tagService.suggestTags(projectId));
		}
		else {
			return ResponseEntity.ok(tagService.suggestTags(projectId, query));
		}
	}
}
