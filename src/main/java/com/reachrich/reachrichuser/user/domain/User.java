package com.reachrich.reachrichuser.user.domain;

import static com.reachrich.reachrichuser.global.util.Const.ROLE_USER;

import com.reachrich.reachrichuser.global.authentication.UserAuthenticationToken;
import com.reachrich.reachrichuser.user.dto.RegisterDto;
import com.reachrich.reachrichuser.user.entity.UserEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Builder
public class User implements Serializable {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String roles;

    public boolean isPasswordMatch(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public static User createNewUser(PasswordEncoder passwordEncoder, RegisterDto registerDto) {
        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
        return User.builder()
            .email(registerDto.getEmail())
            .password(encryptedPassword)
            .nickname(registerDto.getNickname())
            .roles(ROLE_USER)
            .build();
    }

    public UserAuthenticationToken makeAuthentication() {
        List<GrantedAuthority> roles = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(this.roles, ",");
        while (st.hasMoreTokens()) {
            roles.add(new SimpleGrantedAuthority(st.nextToken()));
        }
        return new UserAuthenticationToken(email, password, roles);
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
            .id(id)
            .email(email)
            .password(password)
            .nickname(nickname)
            .roles(roles)
            .build();
    }
}
