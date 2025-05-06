package com.projam.projambackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsByGmailException extends RuntimeException {

	public UserAlreadyExistsByGmailException(String message) {
		super(message);
	}
}
