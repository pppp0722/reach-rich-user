package com.reachrich.reachrichuser.global.util;

public class Const {

    // Redis
    public static final String LOGIN_USER = "loginUser";
    public static final String EMAIL_AUTH = "emailAuth";
    public static final String EMAIL = "email";
    public static final String AUTH_CODE = "authCode";

    // Spring Security
    public static final String ROLE_USER = "ROLE_USER";

    // Email
    public static final int AUTH_MAIL_LIMIT_SECONDS = 180;

    // Random Generator
    public static final int AUTH_CODE_LEN = 6;
    public static final int DIGIT = 0;
    public static final int LOWER_CASE = 1;
    public static final int UPPER_CASE = 2;
    public static final int LOWER_CASE_START = 97;
    public static final int UPPER_CASE_START = 65;
    public static final int ALPHABET_LEN = 26;
}
