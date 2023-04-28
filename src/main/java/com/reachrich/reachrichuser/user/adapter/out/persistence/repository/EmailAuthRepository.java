package com.reachrich.reachrichuser.user.adapter.out.persistence.repository;

import com.reachrich.reachrichuser.user.adapter.out.persistence.entity.EmailAuthEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAuthRepository extends CrudRepository<EmailAuthEntity, String> {

}
