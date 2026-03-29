package com.flightspotter.config;

import com.flightspotter.entity.User;
import com.flightspotter.enums.UserRole;
import com.flightspotter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        String email = "admin@flightspotter.com";

        if (userRepository.existsByEmailAndDeletedFalse(email)) {
            log.info("Dummy user already exists, skipping creation.");
            return;
        }

        User dummyUser = User.builder()
                .username("admin")
                .email(email)
                .passwordHash(passwordEncoder.encode("admin123"))
                .displayName("Admin Spotter")
                .role(UserRole.ROLE_ADMIN)
                .build();

        userRepository.save(dummyUser);
        log.info("Dummy user created — email: {} | password: admin123", email);
    }
}
