package com.reachrich.reachrichuser.global.handler;

import static com.reachrich.reachrichuser.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import com.reachrich.reachrichuser.global.exception.CustomException;
import com.reachrich.reachrichuser.global.exception.ErrorCode;
import com.reachrich.reachrichuser.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    private ResponseEntity<ErrorResponse> handleLoginDeniedException(CustomException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        return handleExceptionInternal(INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
}
