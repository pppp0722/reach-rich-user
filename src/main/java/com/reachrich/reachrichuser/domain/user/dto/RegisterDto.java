package com.reachrich.reachrichuser.domain.user.dto;

import lombok.Getter;

@Getter
public class RegisterDto {

    private String email;
    private String password;
    private String nickname;
    private String authCode;
}
