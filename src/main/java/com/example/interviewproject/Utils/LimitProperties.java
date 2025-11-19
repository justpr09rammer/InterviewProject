package com.example.interviewproject.Utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LimitProperties {
    private final int maxPasswordChangeAttempts = 1;
}
