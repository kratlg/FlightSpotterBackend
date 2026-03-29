package com.flightspotter.serviceImpl;

import com.flightspotter.dto.response.UserResponse;
import com.flightspotter.entity.User;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.exception.AppException;
import com.flightspotter.repository.SpotRepository;
import com.flightspotter.repository.UserRepository;
import com.flightspotter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SpotRepository spotRepository;

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> AppException.notFound(AppMessage.USER_NOT_FOUND));
        return toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> AppException.notFound(AppMessage.USER_NOT_FOUND));
        return toResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> AppException.notFound(AppMessage.USER_NOT_FOUND));
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        long spotCount = spotRepository.countByUserIdAndDeletedFalse(user.getId());
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .role(user.getRole().name())
                .spotCount(spotCount)
                .build();
    }
}
