package com.reachrich.reachrichuser.user.adapter.out.persistence;

import com.reachrich.reachrichuser.user.adapter.out.persistence.entity.UserEntity;
import com.reachrich.reachrichuser.user.adapter.out.persistence.repository.UserRepository;
import com.reachrich.reachrichuser.user.application.port.out.user.CreateUserPort;
import com.reachrich.reachrichuser.user.application.port.out.user.ReadUserPort;
import com.reachrich.reachrichuser.user.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements CreateUserPort, ReadUserPort {

    private final UserRepository userRepository;

    @Override
    public String create(User user) {
        UserEntity userEntity = user.toUserEntity();
        return userRepository.save(userEntity).getNickname();
    }

    @Override
    public Optional<User> readByEmail(String email) {
        Optional<UserEntity> maybeUserEntity = userRepository.findByEmail(email);
        if (maybeUserEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(maybeUserEntity.get().toDomainEntity());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}