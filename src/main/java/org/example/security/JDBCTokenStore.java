package org.example.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.common.security.Authentication;
import org.common.security.UserDetails;
import org.common.security.tokenstore.TokenStoreAdmin;
import org.example.entity.UserAccessToken;
import org.example.repository.UserAccessTokenRepository;
import org.example.util.AppProperties;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class JDBCTokenStore implements TokenStoreAdmin {
    @Autowired
    private UserAccessTokenRepository userAccessTokenRepository;
    @Autowired
    private AppProperties appProperties;

    @Override
    public Authentication getAuthentication(String token) {
        Optional<UserAccessToken> optionalUserAccessToken = userAccessTokenRepository.findFirstByToken(token);
        if (optionalUserAccessToken.isPresent()) {
            UserAccessToken userAccessToken = optionalUserAccessToken.get();
            if (userAccessToken.getExpiredAt().isBefore(LocalDateTime.now())) {
                // the token has expired
                log.info("The session of user [{}-{}] has expired", userAccessToken.getUserId(), userAccessToken.getUsername());
                userAccessTokenRepository.delete(userAccessToken);
                return null;
            } else {
                return buildAuthentication(userAccessToken);
            }
        }
        // token is invalid
        return null;
    }

    private Authentication buildAuthentication(UserAccessToken userAccessToken) {
        return Authentication.builder()
                .token(userAccessToken.getToken())
                .userId(userAccessToken.getUserId())
                .username(userAccessToken.getUsername())
                .authorities(userAccessToken.getAuthorities().split(","))
                .expiredAt(userAccessToken.getExpiredAt())
                .build();
    }

    private UserAccessToken buildUserAccessToken(UserDetails userDetails) {
        return UserAccessToken.builder()
                .token(UUID.randomUUID().toString())
                .username(userDetails.getUsername())
                .userId(userDetails.getUserId())
                .isAdmin(userDetails.isAdmin())
                .authorities(userDetails.getAuthorities())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusSeconds(appProperties.getTokenValiditySeconds()))
                .build();
    }

    @Override
    public Authentication getAuthentication(UserDetails userDetails, boolean allowManyDevices) {
        Optional<UserAccessToken> optionalUserAccessToken = userAccessTokenRepository.findFirstByUserIdAndIsAdmin(userDetails.getUserId(), userDetails.isAdmin());

        if (optionalUserAccessToken.isPresent()) {
            if (allowManyDevices && optionalUserAccessToken.get().getExpiredAt().isAfter(LocalDateTime.now())) {
                // reuse old token in case of allowing many devices to login at the same time
                UserAccessToken userAccessToken = optionalUserAccessToken.get();
                userAccessToken.setExpiredAt(LocalDateTime.now().plusSeconds(appProperties.getTokenValiditySeconds()));
                userAccessToken.setUpdatedAt(LocalDateTime.now());
                return buildAuthentication(userAccessTokenRepository.save(userAccessToken));
            } else {
                // delete old item
                userAccessTokenRepository.delete(optionalUserAccessToken.get());
                // recreate new one
                return buildAuthentication(userAccessTokenRepository.save(buildUserAccessToken(userDetails)));
            }
        } else {
            // create new one and save
            return buildAuthentication(userAccessTokenRepository.save(buildUserAccessToken(userDetails)));
        }
    }
}
