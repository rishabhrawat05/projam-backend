package com.projam.projambackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.services.WeeklyProgressService;

@RestController
@RequestMapping("/projam/weekly-progress")
public class WeekProgressController {

	private final WeeklyProgressService weeklyProgressService;
	
	public WeekProgressController(WeeklyProgressService weeklyProgressService) {
		this.weeklyProgressService = weeklyProgressService;
	}
	
	@GetMapping("/get-weekly-progress")
	public ResponseEntity<?> getWeeklyProgress(@RequestParam String projectId, @RequestParam String email){
		return ResponseEntity.ok(weeklyProgressService.getWeeklyProgress(projectId, email));
	}
}
