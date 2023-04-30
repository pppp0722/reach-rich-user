package com.reachrich.reachrichuser.user.adapter.in.web.dto;

import lombok.Getter;

@Getter
public class RegisterDto {

    private String email;
    private String password;
    private String nickname;
    private String authCode;
}
