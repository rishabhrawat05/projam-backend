package com.projam.projambackend.services;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.projam.projambackend.dto.LoginRequest;
import com.projam.projambackend.dto.LoginResponse;
import com.projam.projambackend.dto.SignupRequest;
import com.projam.projambackend.exceptions.UserAlreadyExistsByGmailException;
import com.projam.projambackend.jwt.JwtHelper;
import com.projam.projambackend.models.User;
import com.projam.projambackend.repositories.UserRepository;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;

	private final UserDetailsService userDetailsService;

	private final JwtHelper jwtHelper;

	public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService, JwtHelper jwtHelper) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.jwtHelper = jwtHelper;
		this.userDetailsService = userDetailsService;
	}

	public LoginResponse login(LoginRequest loginRequest) {
		Authenticate(loginRequest.getGmail(), loginRequest.getPassword());
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getGmail());
		String token = jwtHelper.generateToken(userDetails);
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		return loginResponse;
	}

	public void Authenticate(String gmail, String password) {
		UsernamePasswordAuthenticationToken authenticateToken = new UsernamePasswordAuthenticationToken(gmail,
				password);
		try {
			authenticationManager.authenticate(authenticateToken);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Username or Password");
		}

	}
	
	public String signup(SignupRequest signupRequest) {
		Optional<User> optUser = userRepository.findByGmail(signupRequest.getGmail());
		if(optUser.isPresent()) {
			throw new UserAlreadyExistsByGmailException("User Already Exists with the Gmail");
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setGmail(signupRequest.getGmail());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		return "User Signup Successfull";
		
	}

}
