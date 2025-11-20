package com.example.interviewproject.configuration;

import com.example.interviewproject.model.User;
import com.example.interviewproject.model.UserRole;
import com.example.interviewproject.model.UserStatus;
import com.example.interviewproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminUserInitializer.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final String DEFAULT_ADMIN_EMAIL = System.getenv().getOrDefault("ADMIN_EMAIL", "admin@gmail.com");
    private static final String DEFAULT_ADMIN_PASSWORD = System.getenv().getOrDefault("ADMIN_PASSWORD", "admin1234");
    private static final String DEFAULT_ADMIN_USERNAME = System.getenv().getOrDefault("ADMIN_USERNAME", "admin");
    private static final String DEFAULT_ADMIN_PHONE = System.getenv().getOrDefault("ADMIN_PHONE", "+994501234567");

    @Override
    public void run(String... args) {
        try {
            createDefaultAdminIfNotExists();
        } catch (Exception e) {
            logger.error("Failed to create default admin user", e);
        }
    }

    private void createDefaultAdminIfNotExists() {
        if (userRepository.findByEmail(DEFAULT_ADMIN_EMAIL).isPresent()) {
            logger.info("Admin user already exists with email: {}", DEFAULT_ADMIN_EMAIL);
            return;
        }

        User admin = User.builder()
                .name("Admin")
                .surname("User")
                .username(DEFAULT_ADMIN_USERNAME)
                .email(DEFAULT_ADMIN_EMAIL)
                .phone(DEFAULT_ADMIN_PHONE)
                .password(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD))
                .userRole(UserRole.ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .enabled(true)
                .passwordChangeAttempts(0)
                .build();

        userRepository.save(admin);
        logger.info("✓ Default admin user created successfully!");
        logger.info("  Email: {}", DEFAULT_ADMIN_EMAIL);
        logger.info("  Username: {}", DEFAULT_ADMIN_USERNAME);
        logger.info("  Role: ADMIN");
        logger.warn("⚠ IMPORTANT: Change the default admin password after first login!");
    }
}