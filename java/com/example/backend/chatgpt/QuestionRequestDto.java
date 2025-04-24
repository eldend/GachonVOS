package com.example.backend.chatgpt;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class QuestionRequestDto implements Serializable {
    private String question;
}