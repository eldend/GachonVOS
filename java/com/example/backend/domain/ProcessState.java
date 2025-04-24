package com.example.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessState {
    처리대기("CATEGORY_처리대기"),
    처리중("CATEGORY_처리중"),
    처리완료("CATEGORY_처리완료");

    private final String value;
}