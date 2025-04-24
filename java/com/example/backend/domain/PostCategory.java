package com.example.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostCategory {

    학사("CATEGORY_학사"),
    시설("CATEGORY_시설"),
    학교생활("CATEGORY_학교생활"),
    정책제안("CATEGORY_정책제안"),
    기타("CATEGORY_기타");

    private final String value;
}

