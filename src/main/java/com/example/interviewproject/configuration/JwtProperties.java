package com.example.interviewproject.configuration;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class JwtProperties {
    @Value("${security.jwt.expiration-time:3600000}")
    private Integer expirationInMinutes;

    @Value("${security.jwt.secret-key:}")
    private String secret;
}
