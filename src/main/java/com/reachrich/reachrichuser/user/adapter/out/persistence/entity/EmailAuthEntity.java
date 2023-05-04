package com.reachrich.reachrichuser.user.adapter.out.persistence.entity;

import static com.reachrich.reachrichuser.global.util.Constants.AUTH_EMAIL_LIMIT_SECONDS;

import com.reachrich.reachrichuser.user.domain.EmailAuth;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "emailAuth", timeToLive = AUTH_EMAIL_LIMIT_SECONDS)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class EmailAuthEntity {

    @Id
    private String email;

    private String authCode;

    public static EmailAuthEntity fromDomainEntity(EmailAuth emailAuth) {
        return builder()
            .email(emailAuth.getEmail())
            .authCode(emailAuth.getAuthCode())
            .build();
    }

    public EmailAuth toDomainEntity() {
        return EmailAuth.builder()
            .email(email)
            .authCode(authCode)
            .build();
    }
}
