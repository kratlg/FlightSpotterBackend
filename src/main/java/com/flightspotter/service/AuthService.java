package com.flightspotter.service;

import com.flightspotter.dto.request.LoginRequest;
import com.flightspotter.dto.request.RegisterRequest;
import com.flightspotter.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
