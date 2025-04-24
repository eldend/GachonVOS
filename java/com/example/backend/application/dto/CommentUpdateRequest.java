package com.example.backend.application.dto;

import com.example.backend.domain.ProcessState;
import lombok.Data;

import java.util.List;

@Data
public class CommentUpdateRequest {
    private List<CommentDto.CommentUpdateDto> comments;
    private ProcessState processState;
}

