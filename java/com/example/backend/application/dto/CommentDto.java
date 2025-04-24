package com.example.backend.application.dto;

import com.example.backend.domain.Comment;
import com.example.backend.domain.Posts;
import com.example.backend.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * request, response DTO 클래스를 하나로 묶어 InnerStaticClass로 한 번에 관리
 */
public class CommentDto {

    /** 댓글 Service 요청을 위한 DTO 클래스 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long id;
        private String comment;
        private String dept;
        private String tel;
        private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        private User user;
        private Posts posts;
        /* Dto -> Entity */
        public Comment toEntity() {
            Comment comments = Comment.builder()
                    .id(id)
                    .comment(comment)
                    .dept(dept)
                    .tel(tel)
                    .createdDate(createdDate)
                    .modifiedDate(modifiedDate)
                    .user(user)
                    .posts(posts)
                    .build();

            return comments;
        }
    }

    /**
     * 댓글 정보를 리턴할 응답(Response) 클래스
     * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
     */
    @RequiredArgsConstructor
    @Getter
    public static class Response {
        private Long id;
        private String comment;
        private String dept;
        private String tel;
        private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        private int hakbun;
        private Long userId;
        private Long postsId;
        /* Entity -> Dto*/
        public Response(Comment comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();
            this.dept = comment.getDept();
            this.tel = comment.getTel();
            this.createdDate = comment.getCreatedDate();
            this.modifiedDate = comment.getModifiedDate();
            this.hakbun = comment.getUser().getHakbun();
            this.userId = comment.getUser().getId();
            this.postsId = comment.getPosts().getId();
        }
    }

    @Data
    public static class CommentUpdateDto {
        //private Long id;
        private String comment;
        private String dept;
        private String tel;
        // 필요에 따라 다른 필드 추가

    }
}