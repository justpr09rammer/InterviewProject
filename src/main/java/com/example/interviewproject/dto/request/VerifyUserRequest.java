package com.example.interviewproject.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserRequest {
    private String email;
    private String verificationCode;
}