package com.reachrich.reachrichuser.user.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
}
