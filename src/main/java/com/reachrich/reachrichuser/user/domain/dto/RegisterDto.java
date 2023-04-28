package com.reachrich.reachrichuser.user.domain.dto;

import lombok.Getter;

@Getter
public class RegisterDto {

    private String email;
    private String password;
    private String nickname;
    private String authCode;
}
