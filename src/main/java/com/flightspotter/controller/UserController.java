package com.flightspotter.controller;

import com.flightspotter.controller.base.BaseController;
import com.flightspotter.dto.common.ApiResponse;
import com.flightspotter.dto.response.UserResponse;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ok(AppMessage.USER_FOUND, userService.getUserByEmail(userDetails.getUsername()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        return ok(AppMessage.USER_FOUND, userService.getUserById(userId));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        return ok(AppMessage.USER_FOUND, userService.getUserByUsername(username));
    }
}
