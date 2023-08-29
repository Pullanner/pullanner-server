package com.pullanner.domain.user;

import com.pullanner.web.controller.oauth2.dto.OAuth2Provider;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndProvider(String email, OAuth2Provider provider);

    Optional<User> findByNickName(String nickName);

    @EntityGraph(value = "UserWithWorkouts")
    Optional<User> findWithWorkoutsById(Long id);
}
