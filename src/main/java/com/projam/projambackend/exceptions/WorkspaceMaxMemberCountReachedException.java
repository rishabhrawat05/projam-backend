package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WorkspaceMaxMemberCountReachedException extends RuntimeException {

	public WorkspaceMaxMemberCountReachedException(String message) {
		super(message);
	}
}
