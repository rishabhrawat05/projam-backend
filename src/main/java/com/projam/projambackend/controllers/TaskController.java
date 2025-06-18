package com.projam.projambackend.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.TaskRequest;
import com.projam.projambackend.dto.TaskResponse;
import com.projam.projambackend.dto.TaskStatusDto;
import com.projam.projambackend.services.TaskService;

@RestController
@RequestMapping("/projam/task")
public class TaskController {

	private final TaskService taskService;
	
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest, @RequestParam Long projectId){
		return ResponseEntity.ok(taskService.addTask(taskRequest, projectId));
	}
	
	@GetMapping("/get")
	public Page<TaskResponse> getAllTaskByProject(@RequestParam Long projectId, @RequestParam int page, @RequestParam int size){
		return taskService.getAllTasksByProjectId(projectId, page, size);
	}
	
	@PostMapping("/update/status")
	public ResponseEntity<?> updateTaskStatus(@RequestBody TaskStatusDto taskStatusDto, @RequestParam Long projectId){
		return ResponseEntity.ok(taskService.updateTaskStatus(taskStatusDto, projectId));
	}
}
