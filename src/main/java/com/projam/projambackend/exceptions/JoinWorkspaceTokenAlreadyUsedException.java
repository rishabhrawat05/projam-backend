package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class JoinWorkspaceTokenAlreadyUsedException extends RuntimeException {

	public JoinWorkspaceTokenAlreadyUsedException(String message) {
		super(message);
	}
}
