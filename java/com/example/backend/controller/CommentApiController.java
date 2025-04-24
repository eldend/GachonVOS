package com.example.backend.controller;

import com.example.backend.application.dto.PostsDto;
import com.example.backend.application.service.CommentService;
import com.example.backend.application.dto.CommentDto;
import com.example.backend.application.dto.UserDto;
import com.example.backend.application.service.PostsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API Controller
 */
@RequiredArgsConstructor
@RequestMapping("/api/post")
@RestController
public class CommentApiController {

    private final CommentService commentService;
    private final PostsService postService;

    /* CREATE */
    @PostMapping("/{id}/comment")
    public ResponseEntity<Long> save(@PathVariable Long id, @RequestBody CommentDto.Request dto,
                                     @AuthenticationPrincipal UserDto.Response userSessionDto) {
        return ResponseEntity.ok(commentService.save(id, userSessionDto.getHakbun(), dto));
    }//dept, tel, comment, processState(post의 processState.) 를 저장해야함, @AuthenticationPrincipal없이 구현해야함.

    /* READ */
    @GetMapping("/{id}/comment") //OK
    public List<CommentDto.Response> read(@PathVariable Long id) {
        return commentService.findAll(id);
    }

    @PutMapping("/{postsId}/comment")
    public ResponseEntity<Long> updatePostAndComment(@PathVariable Long postsId, @RequestBody UpdateRequest updateRequest) {
        commentService.updateComments(postsId, updateRequest.getCommentUpdateDto());
        postService.updateProcessState(postsId, updateRequest.getPostsUpdateDto().getProcessState());
        return ResponseEntity.ok(postsId);
    }

    @Data
    public static class UpdateRequest {
        private CommentDto.CommentUpdateDto commentUpdateDto;
        private PostsDto.Request postsUpdateDto;
    }


    /* DELETE */
    @DeleteMapping("/{postsId}/comment/{id}") //OK
    public ResponseEntity<Long> delete(@PathVariable Long postsId, @PathVariable Long id) {
        commentService.delete(postsId, id);
        return ResponseEntity.ok(id);
    }
}
