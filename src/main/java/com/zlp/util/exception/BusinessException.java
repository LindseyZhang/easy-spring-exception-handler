package com.zlp.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    public static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object modelInfo;

    public BusinessException(String errorCode) {
        this(errorCode, DEFAULT_HTTP_STATUS, null);
    }

    public BusinessException(String errorCode, HttpStatus httpStatus) {
        this(errorCode, httpStatus, null);
    }

    public BusinessException(String errorCode, Object modelInfo) {
        this(errorCode, DEFAULT_HTTP_STATUS, modelInfo);
    }

    public BusinessException(String errorCode, HttpStatus httpStatus, Object modelInfo) {
        super(errorCode);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.modelInfo = modelInfo;
    }
}
