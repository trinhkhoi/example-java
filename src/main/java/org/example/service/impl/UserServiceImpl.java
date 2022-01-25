package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.common.exception.BusinessException;
import org.common.security.Authentication;
import org.common.security.encoder.PasswordEncoder;
import org.common.security.tokenstore.TokenStoreAdmin;
import org.example.dto.request.CreateUserRequest;
import org.example.repository.UserRepository;
import org.example.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenStoreAdmin tokenStoreAdmin;

    @Override
    public User getCurrentUser() throws BusinessException {
        Optional<User> optionalUser = userRepository.findById(getAuthentication().getUserId());
        if (!optionalUser.isPresent()) {
            throw new BusinessException("User was not found. Please contact administrator to be supported");
        }
        return optionalUser.get();
    }

    @Override
    public void createUser(CreateUserRequest request) throws BusinessException {
        if (!request.isValid()) {
            throw new BusinessException("Bad request");
        }
        if (userRepository.findFirstByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException("The user already exists");
        }
        userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .authorities(request.getAuthorities())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Override
    public Authentication login(String username, String rawPassword) throws BusinessException {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(rawPassword)) {
            throw new BusinessException("username and password are required");
        }
        username = username.replaceAll("\\s+", "");
        rawPassword = rawPassword.replaceAll("\\s+", "");
        Optional<User> optionalUser = userRepository.findFirstByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new BusinessException("User was not found");
        }
        if (!passwordEncoder.matches(rawPassword, optionalUser.get().getPassword())) {
            throw new BusinessException("Wrong user credentials");
        }
        return tokenStoreAdmin.getAuthentication(optionalUser.get(), true);
    }

    @Override
    public Authentication checkToken(String accessToken) throws BusinessException {
        return tokenStoreAdmin.getAuthentication(accessToken);
    }
}
