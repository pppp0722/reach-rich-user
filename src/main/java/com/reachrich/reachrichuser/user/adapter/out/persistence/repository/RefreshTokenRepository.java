package com.reachrich.reachrichuser.user.adapter.out.persistence.repository;

import com.reachrich.reachrichuser.user.adapter.out.persistence.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {

}
