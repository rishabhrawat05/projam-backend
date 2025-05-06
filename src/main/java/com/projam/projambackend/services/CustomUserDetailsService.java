package com.projam.projambackend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.projam.projambackend.exceptions.UserNotFoundException;
import com.projam.projambackend.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String gmail) throws UserNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findByGmail(gmail).orElseThrow(() -> new UserNotFoundException("User Not Found Exception"));
	}

}
