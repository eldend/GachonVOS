package com.example.backend.application.dao;

import com.example.backend.domain.Comment;
import com.example.backend.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /* 게시글 댓글 목록 가져오기 */
    List<Comment> getCommentByPostsOrderById(Posts posts);

    Optional<Comment> findByPostsIdAndId(Long postsId, Long id);


    Optional<Comment> findByPostsId(Long postsId);

    List<Comment> findCommentsByPostsId(Long postsId);
}
