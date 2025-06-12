package com.accountmanager.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationApiException extends RuntimeException {
	private final HttpStatus status;

	public AuthenticationApiException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
}
