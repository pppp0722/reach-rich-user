package com.reachrich.reachrichuser.infrastructure.repository;

import com.reachrich.reachrichuser.domain.refreshtoken.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
