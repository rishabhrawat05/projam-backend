package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class JoinWorkspaceRequestNotFound extends RuntimeException {

	public JoinWorkspaceRequestNotFound(String message) {
		super(message);
	}
}
