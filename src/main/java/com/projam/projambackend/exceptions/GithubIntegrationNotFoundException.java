package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GithubIntegrationNotFoundException extends RuntimeException {

	public GithubIntegrationNotFoundException(String message) {
		super(message);
	}
}
