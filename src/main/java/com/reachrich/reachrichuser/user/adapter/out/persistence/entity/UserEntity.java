package com.reachrich.reachrichuser.user.adapter.out.persistence.entity;

import com.reachrich.reachrichuser.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    public User toDomainEntity() {
        return User.builder()
            .id(id)
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }

    public boolean isPasswordMatch(String password) {
        return this.password.equals(password);
    }

    public String getNickname() {
        return nickname;
    }
}
