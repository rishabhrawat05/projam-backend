package com.projam.projambackend.services;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.UserProfileResponse;
import com.projam.projambackend.exceptions.UserNotFoundException;
import com.projam.projambackend.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserProfileResponse getUserByEmail(String email) {
		UserProfileResponse userProfileResponse = userRepository.findUserProfileResponseByGmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		return userProfileResponse;
	}
}
