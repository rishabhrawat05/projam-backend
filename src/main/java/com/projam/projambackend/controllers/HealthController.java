package com.projam.projambackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projam")
public class HealthController {

	@GetMapping("/health")
	public String checkHealth() {
		return "OK";
	}
}
