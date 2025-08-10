package com.projam.projambackend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.TaskColumnRequest;
import com.projam.projambackend.dto.TaskColumnResponse;
import com.projam.projambackend.services.TaskColumnService;

@RestController
@RequestMapping("/projam/task-column")
public class TaskColumnController {

	private final TaskColumnService taskColumnService;
	
	public TaskColumnController(TaskColumnService taskColumnService) {
		this.taskColumnService = taskColumnService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addNewTaskColumn(@RequestBody TaskColumnRequest taskColumnRequest){
		return ResponseEntity.ok(taskColumnService.addColumn(taskColumnRequest));
	}
	
	@GetMapping("/get")
	public List<TaskColumnResponse> getAllTaskColumnByProjectId(@RequestParam String projectId){
		return taskColumnService.getAllTaskColumnByProjectId(projectId);
	}
	
	@GetMapping("/suggest/status")
	public ResponseEntity<List<String>> suggestTaskColumnStatus(@RequestParam String projectId, @RequestParam String query){
		if(query == null) {
			return ResponseEntity.ok(taskColumnService.suggestTaskColumn(projectId));
		}
		else {
			return ResponseEntity.ok(taskColumnService.suggestTaskColumnAndQuery(projectId, query));
		}
	}
	
}
