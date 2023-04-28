package com.reachrich.reachrichuser.user.domain.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 예상치 못한 에러가 발생했습니다."),

    // 사용자 관련
    LOGIN_DENIED(HttpStatus.UNAUTHORIZED, "이메일 혹은 비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    EMAIL_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
    VERIFY_EMAIL_FAILURE(HttpStatus.UNAUTHORIZED, "이메일 인증에 실패했습니다."),

    // JWT 관련
    ACCESS_TOKEN_REISSUE_FAIL(HttpStatus.UNAUTHORIZED, "Access Token 재발급에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
