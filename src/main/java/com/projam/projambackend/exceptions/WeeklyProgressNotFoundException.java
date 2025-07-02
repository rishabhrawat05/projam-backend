package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WeeklyProgressNotFoundException extends RuntimeException {

	public WeeklyProgressNotFoundException(String message) {
		super(message);
	}
}
