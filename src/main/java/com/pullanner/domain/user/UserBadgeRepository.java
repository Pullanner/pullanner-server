package com.pullanner.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    @Query(value = "select ub from UserBadge ub where ub.user.id = :userId and ub.badge.id = 1")
    Optional<UserBadge> findAllRoundBadgeByUserId(@Param("userId") Long userId);
}
