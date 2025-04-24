package com.example.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER"), //사용자, 관리자, 게스트
    ADMIN("ROLE_ADMIN"),
    GUEST("ROLE_GUEST");

    private final String value;
}
