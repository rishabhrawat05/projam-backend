package com.projam.projambackend.controllers;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.http.HttpStatusCodes;
import com.projam.projambackend.dto.GoogleLoginRequest;
import com.projam.projambackend.dto.LoginRequest;
import com.projam.projambackend.dto.RefreshTokenRequest;
import com.projam.projambackend.dto.RefreshTokenResponse;
import com.projam.projambackend.dto.ResendOtpRequest;
import com.projam.projambackend.dto.SignupRequest;
import com.projam.projambackend.dto.VerifyRequest;
import com.projam.projambackend.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/projam/auth")
public class AuthenticationController {

	private AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		return ResponseEntity.ok(authenticationService.login(loginRequest, response));
	}

	@PostMapping("/verify/gmail")
	public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest verifyRequest) {
		return ResponseEntity.ok(authenticationService.verifyOtp(verifyRequest));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
		return ResponseEntity.ok(authenticationService.signup(signupRequest));
	}

	@PostMapping("/resend/otp")
	public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest) {
		return ResponseEntity.ok(authenticationService.resendOtp(resendOtpRequest));
	}

	@PostMapping("/refreshtoken/generate")
	public ResponseEntity<?> generateRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		return ResponseEntity.ok(authenticationService.generateRefreshToken(refreshTokenRequest));
	}

	@PostMapping("/google-login")
	public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest,
			HttpServletResponse response) {
		return ResponseEntity.ok(authenticationService.googleLogin(googleLoginRequest.getAccessToken(), response));
	}

	@PostMapping("/github-login")
	public ResponseEntity<?> githubLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
		String code = request.get("code");
		return ResponseEntity.ok(authenticationService.githubLogin(code, response));
	}

	@PostMapping("/token/refresh")
	public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(request, response);
		return ResponseEntity.ok(refreshTokenResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		String message = authenticationService.logout(request, response);
		return ResponseEntity.ok(message);
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validate(HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.status(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED).body(false);
	}

}
