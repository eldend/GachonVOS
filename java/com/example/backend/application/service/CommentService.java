package com.example.backend.application.service;

import com.example.backend.application.dto.CommentDto;
import com.example.backend.domain.Comment;
import com.example.backend.domain.Posts;
import com.example.backend.domain.User;
import com.example.backend.application.dao.CommentRepository;
import com.example.backend.application.dao.PostsRepository;
import com.example.backend.application.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public void updateComments(Long postsId, CommentDto.CommentUpdateDto commentUpdateDto) {
        // postsId에 해당하는 게시물의 댓글을 가져온다 (예시로 가져오는 메서드 이름은 getCommentsByPostsId)
        List<Comment> comments = commentRepository.findCommentsByPostsId(postsId);

        if (!comments.isEmpty()) {
            Comment comment = comments.get(0);
            comment.setComment(commentUpdateDto.getComment());
            comment.setDept(commentUpdateDto.getDept());
            comment.setTel(commentUpdateDto.getTel());


            commentRepository.save(comment);
        }
    }
    /* CREATE */
    @Transactional
    public Long save(Long id, int hakbun, CommentDto.Request dto) {
        User user = userRepository.findByHakbun(hakbun);
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + id));

        dto.setUser(user);
        dto.setPosts(posts);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return comment.getId();
    }

    /* READ */
    @Transactional(readOnly = true)
    public List<CommentDto.Response> findAll(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + id));
        List<Comment> comments = posts.getComments();
        return comments.stream().map(CommentDto.Response::new).collect(Collectors.toList());
    }

    /* UPDATE */
    @Transactional
    public void update(Long postsId, Long id, CommentDto.Request dto) {
        Comment comment = commentRepository.findByPostsIdAndId(postsId, id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. " + id));

        comment.update(dto.getComment());
    }

    /* DELETE */
    @Transactional
    public void delete(Long postsId, Long id) {
        Comment comment = commentRepository.findByPostsIdAndId(postsId, id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));

        commentRepository.delete(comment);
    }

    //게시글이 등록되면 comments 테이블에 자동으로 들어가도록 초기값 설정!!!!
    @Transactional
    public Comment save(Long postId, Long userId, CommentDto.Request dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id: " + userId));
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + postId));

        dto.setUser(user);
        dto.setPosts(posts);
        dto.setDept("");
        dto.setTel("");

        Comment comment = dto.toEntity();
        return commentRepository.save(comment);
    }
}