package com.reachrich.reachrichuser.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterDto {

    private String email;

    private String password;

    private String nickname;
}
