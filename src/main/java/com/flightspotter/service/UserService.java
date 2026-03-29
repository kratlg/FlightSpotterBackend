package com.flightspotter.service;

import com.flightspotter.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email);
    UserResponse getUserByUsername(String username);
}
