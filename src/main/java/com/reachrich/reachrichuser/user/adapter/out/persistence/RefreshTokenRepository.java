package com.reachrich.reachrichuser.user.adapter.out.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {

}
