package com.zlp.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private final MessageSource messageSource;

	@Autowired
	public GlobalExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler
	public ResponseEntity handleException(Exception e, Locale locale) {
		if (e instanceof BusinessException) {
			BusinessException businessException = (BusinessException) e;
			return ResponseEntity.status(businessException.getHttpStatus())
					.body(toExceptionDetail(businessException, locale));
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
	}

	private ExceptionDetail toExceptionDetail(BusinessException e, Locale locale) {
		return new ExceptionDetail(e.getHttpStatus().value(),
				e.getErrorCode().toString(),
				messageSource.getMessage(e.getErrorCode().getErrorMessage(), null, locale),
				e.getModelInfo());
	}

	@Getter
	@AllArgsConstructor
	public static final class ExceptionDetail {
		private final int status;
		private final String error;
		private final String message;
		private final Object extra;
	}
}
