package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TaskColumnAlreadyExistException extends RuntimeException {

	public TaskColumnAlreadyExistException(String message) {
		super(message);
	}
}
