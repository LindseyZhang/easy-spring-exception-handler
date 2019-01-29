package com.zlp.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity handleException(Exception e) {
		if (e instanceof BusinessException) {
			BusinessException businessException = (BusinessException) e;
			return ResponseEntity.status(businessException.getHttpStatus())
					.body(ExceptionDetail.fromBusinessException(businessException));
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
	}

	@Getter
	@AllArgsConstructor
	public static final class ExceptionDetail {
		private final int status;
		private final String error;
		private final String message;
		private final Object extra;

		static ExceptionDetail fromBusinessException(BusinessException e) {
			return new ExceptionDetail(e.getHttpStatus().value(),
					e.getErrorCode().toString(),
					e.getErrorCode().getErrorMessage(),
					e.getModelInfo());
		}
	}
}
