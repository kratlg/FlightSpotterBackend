package com.flightspotter.controller;

import com.flightspotter.controller.base.BaseController;
import com.flightspotter.dto.common.ApiResponse;
import com.flightspotter.dto.request.LoginRequest;
import com.flightspotter.dto.request.RegisterRequest;
import com.flightspotter.dto.response.AuthResponse;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return created(AppMessage.REGISTER_SUCCESS, authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ok(AppMessage.LOGIN_SUCCESS, authService.login(request));
    }
}
