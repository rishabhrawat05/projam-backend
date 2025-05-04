package com.projam.projambackend.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.projam.projambackend.dto.LoginRequest;
import com.projam.projambackend.dto.LoginResponse;
import com.projam.projambackend.jwt.JwtHelper;
import com.projam.projambackend.repositories.UserRepository;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;
	
	private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtHelper jwtHelper;
	
	public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtHelper jwtHelper) {
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
	        UsernamePasswordAuthenticationToken authenticateToken = new UsernamePasswordAuthenticationToken(gmail, password);
	        try {
	            authenticationManager.authenticate(authenticateToken);
	        } catch (BadCredentialsException e) {
	            throw new BadCredentialsException("Invalid Username or Password");
	        }
	
	    }
}
