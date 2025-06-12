package com.accountmanager.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(AuthenticationApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(AuthenticationApiException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
		return new ResponseEntity<>(errorResponse, ex.getStatus());
	}

	@Getter
	public static class ErrorResponse {
		private final String message;
		private final int status;

		public ErrorResponse(String message, HttpStatus status) {
			this.message = message;
			this.status = status.value();
		}
	}
}
