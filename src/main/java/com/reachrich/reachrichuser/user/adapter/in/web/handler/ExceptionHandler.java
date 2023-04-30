package com.reachrich.reachrichuser.user.adapter.in.web.handler;

import static com.reachrich.reachrichuser.user.domain.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.reachrich.reachrichuser.user.domain.exception.ErrorCode.INVALID_PARAM_REQUEST;

import com.reachrich.reachrichuser.user.domain.exception.CustomException;
import com.reachrich.reachrichuser.user.domain.exception.ErrorCode;
import com.reachrich.reachrichuser.user.domain.exception.ErrorResponse;
import javax.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    private ResponseEntity<ErrorResponse> handleLoginDeniedException(CustomException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<ErrorResponse> handleConstraintViolationException(
        ConstraintViolationException e) {

        return handleExceptionInternal(INVALID_PARAM_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        return handleExceptionInternal(INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
}
