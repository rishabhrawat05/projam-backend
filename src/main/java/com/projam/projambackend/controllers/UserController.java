package com.projam.projambackend.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.UserProfileResponse;
import com.projam.projambackend.services.UserService;

@RestController
@RequestMapping("/projam/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/profile")
	public ResponseEntity<UserProfileResponse> getUserByGmail(Principal principal){
		return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
	}

}
