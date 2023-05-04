package com.reachrich.reachrichuser.user.adapter.out.persistence;

import com.reachrich.reachrichuser.user.adapter.out.persistence.entity.EmailAuthEntity;
import com.reachrich.reachrichuser.user.adapter.out.persistence.repository.EmailAuthRepository;
import com.reachrich.reachrichuser.user.application.port.out.emailauth.CreateEmailAuthPort;
import com.reachrich.reachrichuser.user.application.port.out.emailauth.ReadEmailAuthPort;
import com.reachrich.reachrichuser.user.domain.EmailAuth;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class EmailAuthPersistenceAdapter implements CreateEmailAuthPort, ReadEmailAuthPort {

    private final EmailAuthRepository emailAuthRepository;

    @Override
    public void create(EmailAuth emailAuth) {
        emailAuthRepository.save(EmailAuthEntity.fromDomainEntity(emailAuth));
    }

    @Override
    public Optional<EmailAuth> readByEmail(String email) {
        Optional<EmailAuthEntity> maybeEmailAuthEntity = emailAuthRepository.findById(email);

        if (maybeEmailAuthEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(maybeEmailAuthEntity.get().toDomainEntity());
    }
}
