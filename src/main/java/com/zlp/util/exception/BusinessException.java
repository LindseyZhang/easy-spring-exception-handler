package com.zlp.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    public static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final Object modelInfo;

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = DEFAULT_HTTP_STATUS;
        this.modelInfo = null;
    }

    public BusinessException(ErrorCode errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.modelInfo = null;
    }

    public BusinessException(ErrorCode errorCode, Object modelInfo) {
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.modelInfo = modelInfo;
    }

    public BusinessException(ErrorCode errorCode, HttpStatus httpStatus, Object modelInfo) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.modelInfo = modelInfo;
    }
}
