package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RefreshTokenExpiredException extends RuntimeException {

	public RefreshTokenExpiredException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
}
