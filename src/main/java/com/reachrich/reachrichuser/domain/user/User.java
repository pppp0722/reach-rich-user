package com.reachrich.reachrichuser.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    public boolean isPasswordMatch(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public String getNickname() {
        return nickname;
    }

    public static User registerNewUser(PasswordEncoder passwordEncoder, RegisterDto registerDto) {
        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
        return User.builder()
            .email(registerDto.getEmail())
            .password(encryptedPassword)
            .nickname(registerDto.getNickname())
            .build();
    }
}
