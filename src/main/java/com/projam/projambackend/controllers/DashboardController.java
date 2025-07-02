package com.projam.projambackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.services.DashboardService;

@RestController
@RequestMapping("/projam/dashboard")
public class DashboardController {
	
	private final DashboardService dashboardService;
	
	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/get-dashboard")
	public ResponseEntity<?> getDashboard(@RequestParam String projectId, @RequestParam String email){
		return ResponseEntity.ok(dashboardService.getDashboard(projectId, email));
	}
}
