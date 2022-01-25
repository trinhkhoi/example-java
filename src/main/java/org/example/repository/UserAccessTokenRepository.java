package org.example.repository;

import org.example.entity.UserAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccessTokenRepository extends JpaRepository<UserAccessToken, Long> {
    Optional<UserAccessToken> findFirstByUserIdAndIsAdmin(Long userId, Boolean isAdmin);

    Optional<UserAccessToken> findFirstByToken(String token);
}
