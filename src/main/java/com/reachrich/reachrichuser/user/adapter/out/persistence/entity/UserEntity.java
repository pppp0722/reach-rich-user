package com.reachrich.reachrichuser.user.adapter.out.persistence.entity;

import com.reachrich.reachrichuser.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "role",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private List<RoleVo> roles = new ArrayList<>();

    public static UserEntity fromDomainEntity(User user) {
        return builder()
            .id(user.getId())
            .email(user.getEmail())
            .password(user.getPassword())
            .nickname(user.getNickname())
            .roles(
                user.getRoles().stream()
                    .map(RoleVo::from)
                    .collect(Collectors.toList())
            )
            .build();
    }

    public User toDomainEntity() {
        return User.builder()
            .id(id)
            .email(email)
            .password(password)
            .nickname(nickname)
            .roles(
                roles.stream()
                    .map(RoleVo::getRole)
                    .collect(Collectors.toList())
            )
            .build();
    }
}
