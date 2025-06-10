package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProjectNameAlreadyExistException extends RuntimeException {

	public ProjectNameAlreadyExistException(String message) {
		super(message);
	}
}
