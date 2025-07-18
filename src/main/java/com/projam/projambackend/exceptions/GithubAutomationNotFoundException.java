package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GithubAutomationNotFoundException extends RuntimeException {

	public GithubAutomationNotFoundException(String message) {
		super(message);
	}
}
