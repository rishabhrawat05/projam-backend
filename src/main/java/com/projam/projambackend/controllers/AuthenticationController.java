package com.projam.projambackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.LoginRequest;
import com.projam.projambackend.dto.SignupRequest;
import com.projam.projambackend.services.AuthenticationService;

@RestController
@RequestMapping("/projam")
public class AuthenticationController {

	private AuthenticationService authenticationService;
	
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
		return ResponseEntity.ok(authenticationService.login(loginRequest));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
		return ResponseEntity.ok(authenticationService.signup(signupRequest));
	}
}
