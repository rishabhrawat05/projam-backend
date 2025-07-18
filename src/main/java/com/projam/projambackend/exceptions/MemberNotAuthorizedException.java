package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class MemberNotAuthorizedException extends RuntimeException {

	public MemberNotAuthorizedException(String message) {
		super(message);
	}
}
