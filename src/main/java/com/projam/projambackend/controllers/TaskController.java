package com.projam.projambackend.controllers;

import java.util.List;

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
import com.projam.projambackend.dto.TaskSearchRequest;
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
	public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest, @RequestParam String projectId){
		return ResponseEntity.ok(taskService.addTask(taskRequest, projectId));
	}
	
	@GetMapping("/get")
	public Page<TaskResponse> getAllTaskByProject(@RequestParam String projectId, @RequestParam int page, @RequestParam int size){
		return taskService.getAllTasksByProjectId(projectId, page, size);
	}
	
	@PostMapping("/update/status")
	public ResponseEntity<?> updateTaskStatus(@RequestBody TaskStatusDto taskStatusDto, @RequestParam String projectId){
		return ResponseEntity.ok(taskService.updateTaskStatus(taskStatusDto, projectId));
	}
	
	@GetMapping("/tasks")
	public Page<TaskResponse> getTasksForColumn(
	    @RequestParam String columnId,
	    @RequestParam String projectId,
	    @RequestParam int page,
	    @RequestParam int size
	) {
	    return taskService.getPaginatedTasks(columnId, projectId, page, size);
	}
	
	@PutMapping("/delete")
	public void deleteTask(@RequestParam String taskId) {
		taskService.deleteTask(taskId);
	}
	
	@GetMapping("/suggest/title")
	public ResponseEntity<List<String>> suggestTitle(@RequestParam String projectId, @RequestParam String query){
		if(query == null) {
			return ResponseEntity.ok(taskService.suggestTitleByProjectId(projectId));
		}
		else {
			return ResponseEntity.ok(taskService.suggestTitleByProjectIdAndQuery(projectId, query));
		}
	}
	
	@PostMapping("/search")
	public ResponseEntity<List<TaskResponse>> searchTask(@RequestParam String projectId, @RequestBody TaskSearchRequest taskSearchRequest){
		return ResponseEntity.ok(taskService.getTaskByQuery(projectId, taskSearchRequest));
	}
	
	@GetMapping("/get/count")
	public ResponseEntity<Long> getTaskCountByTaskColumnIdAndProjectId(@RequestParam String projectId, @RequestParam String taskColumnId){
		return ResponseEntity.ok(taskService.getCountByTaskColumnIdAndProjectId(projectId, taskColumnId));
	}

}
