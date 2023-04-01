package com.reachrich.reachrichuser.user.domain;

import com.reachrich.reachrichuser.user.dto.RegisterDto;
import com.reachrich.reachrichuser.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String email;
    private String password;
    private String nickname;

    public boolean isPasswordMatch(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public String getNickname() {
        return nickname;
    }

    public static User createNewUser(PasswordEncoder passwordEncoder, RegisterDto registerDto) {
        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
        return User.builder()
            .email(registerDto.getEmail())
            .password(encryptedPassword)
            .nickname(registerDto.getNickname())
            .build();
    }

    public static User fromEntity(UserEntity userEntity) {
        return User.builder()
            .id(userEntity.getId())
            .email(userEntity.getEmail())
            .password(userEntity.getPassword())
            .nickname(userEntity.getNickname())
            .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
            .id(id)
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }
}
