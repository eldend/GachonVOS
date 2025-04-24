package com.example.backend.controller;

import com.example.backend.application.dao.PostsRepository;
import com.example.backend.application.service.PostsService;
import com.example.backend.application.dto.PostsDto;
import com.example.backend.application.dto.UserDto;
import com.example.backend.application.service.UserService;
import com.example.backend.config.AdminAuthorize;
import com.example.backend.domain.Posts;
import com.example.backend.domain.User;
import com.example.backend.security.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST API Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "게시글 API")
public class PostsApiController {

    private final PostsService postsService;
    private final UserService userService;
    private final PostsRepository postsRepository;

    /* 전체 게시물 조회 *///OK
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시글 저장 성공",
            content = @Content(schema = @Schema(implementation = PostsDto.Response.class))),
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"), @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")})
    @GetMapping
    public ResponseEntity<?> readFull() {
        try {
            List<PostsDto.Response> postList = postsService.findFull();
            if (postList == null)
                log.info("조회된 게시글이 없음");
            else
                log.info("게시글 수 : " + postList.size());

            return new ResponseEntity<>(postList, HttpStatus.OK);
        } catch (Exception e) {
            log.error(null, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 사용자가 등록한 게시물들 조회 */ //OK ex. 로그인 후 '홍길동님'버튼 누르면 홍길동이 작성한 글들 나옴 프론트에서 사용가능!!!
    @GetMapping("/user/{id}")
    public ResponseEntity<List<PostsDto.Response>> getUserPosts(@PathVariable Long id) {
        try {
            List<PostsDto.Response> postList = postsService.findFullofUserId(id);

            if (postList == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 사용자 ID에 해당하는 게시물이 없는 경우 404 반환
            }

            return new ResponseEntity<>(postList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while processing user posts request for user ID: " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping//OK
    public ResponseEntity<Long> savePost(@RequestBody PostsDto.Request dto, @AuthenticationPrincipal MyUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userService.findByUsername(username);
            User user = userOptional.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. username: " + username));

            Long postId = postsService.savePost(dto, user.getId());
            return ResponseEntity.ok(postId);
        } else {
            // 사용자가 인증되지 않았을 경우의 처리 (예: 익명 사용자)
            // 여기에 필요한 코드를 추가하세요.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /* READ */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostsDto.Response.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/{id}")//OK
    public ResponseEntity<PostsDto.Response> read(@PathVariable Long id) {
        return ResponseEntity.ok(postsService.findById(id));
    }

    /* DELETE */ //수정필요 로그인한 사용자의 id와 게시물의 userid가 같아야만 삭제 동작 수행(OK). 관리자권한으로 삭제(X)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @DeleteMapping("/{id}") // OK
    public ResponseEntity<Long> delete(@PathVariable Long id) {
//        try {
//            PostsDto.Response post = postsService.findById(id);
//            String username = userDetails.getUsername();
//            Optional<User> user = userService.findByUsername(username);

//            if (user.isPresent() && user.get().getId().equals(post.getUserId()) || userDetails.getAuthorities().contains("ROLE_ADMIN")) {
//                postsService.delete(id);
//                return ResponseEntity.ok(id);
//            } else {
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } catch (Exception e) {
//            // 로깅을 통해 예외 정보를 기록
//            log.error("게시글 삭제 중 예외 발생", e);
//            // 클라이언트에게 예외 정보를 전달하지 않도록 500 에러만 반환
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        try {
            PostsDto.Response post = postsService.findById(id);
            postsService.delete(id);
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            // 로깅을 통해 예외 정보를 기록
            log.error("게시글 삭제 중 예외 발생", e);
            // 클라이언트에게 예외 정보를 전달하지 않도록 500 에러만 반환
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
//            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
//    })
//    @DeleteMapping("/{id}")//OK
//    public ResponseEntity<Long> delete(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails userDetails) {
//        PostsDto.Response post = postsService.findById(id);
//        String username = userDetails.getUsername();
//        Optional<User> user = userService.findByUsername(username);
//
//        if(user.get().getId().equals(post.getUserId()) || userDetails.getAuthorities().contains("ROLE_ADMIN")) {//userDetails.getAuthorities().contains("ADMIN")
//            postsService.delete(id);
//            return ResponseEntity.ok(id);
//        }else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    /* UPDATE */ //게시글 수정기능 없음.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PutMapping("/{id}")//
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody PostsDto.Request dto) {
        postsService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    //
//    /* 사용자가 등록한 게시물들 조회 */ //test OK
//    @GetMapping("/user/username")
//    public ResponseEntity<?> readFullUser() {
//        String username = "username2";
//        try {
//            List<PostsDto.Response> postList = postsService.findFullofUserId(username);
//            if (postList == null)
//                log.info("조회된 게시글이 없음");
//            else
//                log.info("게시글 수 : " + postList.size());
//
//            return new ResponseEntity<>(postList, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(null, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    //@RequestMapping("/api")
//@RequiredArgsConstructor
//@RestController
//public class PostsApiController {
//
//    private final PostsService postsService;
//
//    /* CREATE */
//    @PostMapping("/posts")
//    public ResponseEntity save(@RequestBody PostsDto.Request dto,  UserDto.Response user) {
//        return ResponseEntity.ok(postsService.save(dto, user.getNickname()));
//    }
//
//    /* READ */
//    @GetMapping("/posts/{id}")
//    public ResponseEntity<?> read(@PathVariable Long id) {
//        return ResponseEntity.ok(postsService.findById(id));
//    }
//
//    /* UPDATE */
//    @PutMapping("/posts/{id}")
//    public ResponseEntity update(@PathVariable Long id, @RequestBody PostsDto.Request dto) {
//        postsService.update(id, dto);
//        return ResponseEntity.ok(id);
//    }
//
//    /* DELETE */
//    @DeleteMapping("/posts/{id}")
//    public ResponseEntity delete(@PathVariable Long id) {
//        postsService.delete(id);
//        return ResponseEntity.ok(id);
//    }
//}
}
