package com.reachrich.reachrichuser.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants { // 객체를 생성할 수 없는 클래스의 이름은 보통 복수형 명사로 짓는다. - 이펙티브 자바

    // JWT
    public static final String ACCESS_TOKEN_HEADER = "Access-Token";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final long ACCESS_TOKEN_EXPIRY_SECONDS = 1800;
    public static final long REFRESH_TOKEN_EXPIRY_SECONDS = 1209600;
    public static final long EXPIRED_REFRESH_TOKEN_EXPIRY_SECONDS = 0;
    public static final String EMPTY_REFRESH_TOKEN_VALUE = "empty";
    public static final String ROLES = "roles";

    public enum JwtType {
        ACCESS_TOKEN, REFRESH_TOKEN, EXPIRED_REFRESH_TOKEN
    }

    // Redis
    public static final String EMAIL_AUTH = "emailAuth";
    public static final String EMAIL = "email";
    public static final String AUTH_CODE = "authCode";

    // Email
    public static final String EMAIL_SUBJECT = "[Reach Rich] 회원가입 인증코드 발송";
    public static final int AUTH_EMAIL_LIMIT_SECONDS = 180;

    // Random Generator
    public static final int AUTH_CODE_LEN = 6;
    public static final int DIGIT = 0;
    public static final int LOWER_CASE = 1;
    public static final int UPPER_CASE = 2;
    public static final int LOWER_CASE_START = 97;
    public static final int UPPER_CASE_START = 65;
    public static final int ALPHABET_LEN = 26;
}
