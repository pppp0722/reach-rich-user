package com.reachrich.reachrichuser.user.domain;

import com.reachrich.reachrichuser.user.adapter.out.persistence.entity.UserEntity;
import lombok.Builder;

@Builder
public class User {

    private Long id;

    private String email;

    private String password;

    private String nickname;

    public static User of(String email, String password, String nickname) {
        return builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity toUserEntity() {
        return UserEntity.builder()
            .id(id)
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }
}
