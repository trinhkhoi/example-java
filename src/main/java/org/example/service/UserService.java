package org.example.service;

import org.example.dto.request.CreateUserRequest;
import org.example.entity.User;
import org.common.exception.BusinessException;
import org.common.security.Authentication;

public interface UserService {
    User getCurrentUser() throws BusinessException;

    void createUser(CreateUserRequest request) throws BusinessException;

    Authentication login(String username, String rawPassword) throws BusinessException;

    Authentication checkToken(String accessToken) throws BusinessException;
}
