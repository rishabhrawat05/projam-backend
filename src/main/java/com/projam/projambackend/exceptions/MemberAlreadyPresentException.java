package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MemberAlreadyPresentException extends RuntimeException {

	public MemberAlreadyPresentException(String message) {
		super(message);
	}
}
