package com.example.backend.application.service;

import com.example.backend.application.dto.CommentDto;
import com.example.backend.application.dto.PostsDto;
import com.example.backend.application.dto.UserDto;
import com.example.backend.domain.*;
import com.example.backend.application.dao.PostsRepository;
import com.example.backend.application.dao.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @Transactional
    public void updateProcessState(Long postsId, ProcessState processState) {
        // postsId에 해당하는 게시물을 가져온다 (예시로 가져오는 메서드 이름은 getPostsById)
        Optional<Posts> optionalPosts = postsRepository.findById(postsId);

        // 가져온 게시물이 존재하면 ProcessState를 업데이트하고 저장한다
        optionalPosts.ifPresent(posts -> {
            posts.setProcessState(processState);
            postsRepository.save(posts);
        });
    }
    @Transactional
    public Long savePost(PostsDto.Request dto, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id: " + userId));

        dto.setUser(user);

        PostCategory postCategoryValue = dto.getPostCategory();
        if (postCategoryValue != null) {
            dto.setPostCategory(postCategoryValue);
        } else {
            dto.setPostCategory(PostCategory.기타);
        }

        //**** comments에 자동으로 데이터 넣으려고 추가한 부분 *****
        log.info("PostsService save() 실행");
        Posts posts = dto.toEntity();
        postsRepository.save(posts);

        // 새로운 게시글이 작성되면 해당 게시글의 ID를 이용해 댓글 테이블에 기본 댓글 추가
        CommentDto.Request commentDto = new CommentDto.Request();
        commentDto.setComment("건의 처리 후 답변드리겠습니다.");
        commentDto.setPosts(posts); // 새로운 게시글에 대한 댓글 추가
        //commentDto.setUser(user); // user_id를 1로 설정

        Comment comment = commentService.save(posts.getId(), 1L, commentDto); // user_id가 userId인 댓글 추가
        //***************************

        return posts.getId();
    }

    @Transactional(readOnly = true)
    public List<PostsDto.Response> findFull() {
        List<Posts> posts = postsRepository.findAll();

        return PostsDto.fromEntityList(posts);
    }

    /* READ 게시글 리스트 조회 readOnly 속성으로 조회속도 개선 */
    @Transactional(readOnly = true)
    public PostsDto.Response findById(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + id));

        return new PostsDto.Response(posts);
    }

    /* UPDATE (dirty checking 영속성 컨텍스트)
     *  User 객체를 영속화시키고, 영속화된 User 객체를 가져와 데이터를 변경하면
     * 트랜잭션이 끝날 때 자동으로 DB에 저장해준다. */
    @Transactional
    public void update(Long id, PostsDto.Request dto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        posts.update(dto.getTitle(), dto.getContent());
    }

    /* DELETE */
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        postsRepository.delete(posts);
    }

    /* Views Counting */
    @Transactional
    public int updateView(Long id) {
        return postsRepository.updateView(id);
    }


    /* Paging and Sort */
    @Transactional(readOnly = true)
    public Page<Posts> pageList(Pageable pageable) {
        return postsRepository.findAll(pageable);
    }

    /* search */
    @Transactional(readOnly = true)
    public Page<Posts> search(String keyword, Pageable pageable) {
        Page<Posts> postsList = postsRepository.findByTitleContaining(keyword, pageable);
        return postsList;
    }

    public List<PostsDto.Response> findFullofUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        List<Posts> posts = postsRepository.findAllByUserId(user.getId());
        return PostsDto.fromEntityList(posts);
    }
}
