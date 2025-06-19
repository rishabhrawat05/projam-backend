package com.projam.projambackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.TaskColumnRequest;
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
}
