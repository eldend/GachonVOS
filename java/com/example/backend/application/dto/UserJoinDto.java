package com.example.backend.application.dto;

import com.example.backend.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDto {

    private String username;
    private String password;
    private int hakbun;
    private String email;
    private Role role;
}