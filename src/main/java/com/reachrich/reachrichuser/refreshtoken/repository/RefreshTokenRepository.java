package com.reachrich.reachrichuser.refreshtoken.repository;

import com.reachrich.reachrichuser.refreshtoken.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {

}
