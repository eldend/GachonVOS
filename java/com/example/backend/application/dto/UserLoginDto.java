package com.example.backend.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Request-
 * 로그인 요청 dto
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLoginDto {

    private String username;
    private String password;

    @Builder
    public UserLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
