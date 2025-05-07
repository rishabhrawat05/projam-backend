package com.projam.projambackend.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.projam.projambackend.config.PasswordEncoder;
import com.projam.projambackend.dto.LoginRequest;
import com.projam.projambackend.dto.LoginResponse;
import com.projam.projambackend.dto.ResendOtpRequest;
import com.projam.projambackend.dto.SignupRequest;
import com.projam.projambackend.dto.VerifyRequest;
import com.projam.projambackend.email.EmailUtility;
import com.projam.projambackend.enums.Role;
import com.projam.projambackend.exceptions.EmailNotVerifiedException;
import com.projam.projambackend.exceptions.UserAlreadyExistsByGmailException;
import com.projam.projambackend.exceptions.UserNotFoundException;
import com.projam.projambackend.jwt.JwtHelper;
import com.projam.projambackend.models.User;
import com.projam.projambackend.repositories.UserRepository;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;

	private final UserDetailsService userDetailsService;

	private final JwtHelper jwtHelper;
	
	private final EmailUtility emailUtility;
	
	private final PasswordEncoder passwordEncoder;

	public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService, JwtHelper jwtHelper, EmailUtility emailUtility, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.jwtHelper = jwtHelper;
		this.userDetailsService = userDetailsService;
		this.emailUtility = emailUtility;
		this.passwordEncoder = passwordEncoder;
	}

	public LoginResponse login(LoginRequest loginRequest) {
		Authenticate(loginRequest.getGmail(), loginRequest.getPassword());
		User user = userRepository.findByGmail(loginRequest.getGmail()).orElseThrow(() -> new UserNotFoundException("User Not Found Exception with Email:" + loginRequest.getGmail()));
		if(!user.isVerified()) {
			throw new EmailNotVerifiedException("Email Not Verified!! Please Verify Your Email");
		}
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
		String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(signupRequest.getPassword());
		String otp = generateOtp();
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(Role.FREE);
		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setGmail(signupRequest.getGmail());
		user.setPassword(encodedPassword);
		user.setOtp(otp);
		user.setRoles(roleSet);
		user.setVerified(false);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);
		emailUtility.sendEmail(signupRequest.getGmail(), "Otp Verification for ProJam", "Otp for Verification is:" + otp);
		return "User Signup Successfull";
		
	}
	
	public String generateOtp() {
		return String.valueOf(1000 + new Random().nextInt(9000));
	}
	
	public String resendOtp(ResendOtpRequest resendOtpRequest) {
		Optional<User> optUser = userRepository.findByGmail(resendOtpRequest.getGmail());
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("User Not Found with the Gmail Provided");
		}
		String newOtp = generateOtp();
		User user = optUser.get();
		user.setOtp(newOtp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);
		emailUtility.sendEmail(user.getGmail(), "Otp Verification for ProJam", "Otp for Verification is:" + newOtp);
		return "A new OTP has been sent to your gmail.";
	}
	
	public String verifyOtp(VerifyRequest verifyRequest) {
		Optional<User> optUser = userRepository.findByGmail(verifyRequest.getGmail());
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("User Not Found with the Gmail Provided.");
		}
		User user = optUser.get();
		if(user.getOtp().equals(verifyRequest.getOtp()) && user.getOtpGeneratedTime().plusMinutes(5).isAfter(LocalDateTime.now())) {
			user.setVerified(true);
			user.setOtp(null);
			userRepository.save(user);
			return "Otp Verification Successfull";
		}
		else {
			throw new EmailNotVerifiedException("Email Not Verified By the User");
		}
	}

}
