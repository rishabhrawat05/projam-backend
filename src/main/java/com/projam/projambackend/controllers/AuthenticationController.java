package com.projam.projambackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.projam.projambackend.dto.LoginRequest;
import com.projam.projambackend.dto.RefreshTokenRequest;
import com.projam.projambackend.dto.ResendOtpRequest;
import com.projam.projambackend.dto.SignupRequest;
import com.projam.projambackend.dto.VerifyRequest;
import com.projam.projambackend.services.AuthenticationService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/projam/auth")
public class AuthenticationController {

	private AuthenticationService authenticationService;
	
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
		return ResponseEntity.ok(authenticationService.login(loginRequest, response));
	}
	
	@PostMapping("/verify/gmail")
	public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest){
		return ResponseEntity.ok(authenticationService.verifyOtp(verifyRequest));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
		return ResponseEntity.ok(authenticationService.signup(signupRequest));
	}
	
	@PostMapping("/resend/otp")
	public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest){
		return ResponseEntity.ok(authenticationService.resendOtp(resendOtpRequest));
	}
	
	@PostMapping("/refreshtoken/generate")
	public ResponseEntity<?> generateRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
		return ResponseEntity.ok(authenticationService.generateRefreshToken(refreshTokenRequest));
	}
	
	
}
