package com.example.backend.controller;

import com.example.backend.application.dto.PostsDto;
import com.example.backend.application.dto.UserJoinDto;
import com.example.backend.application.service.RegisterMemberService;
import com.example.backend.application.service.UserService;
import com.example.backend.application.dto.UserDto;
import com.example.backend.config.UserAuthorize;
import com.example.backend.domain.User;
import com.example.backend.security.MyUserDetails;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST API Controller
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserApiController {

    private final UserService userService;
    private final RegisterMemberService registerMemberService;

    @GetMapping("/user")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /* 회원정보 조회 */ //OK
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> Read(@PathVariable Long id) {
        UserDto findUser = userService.read(id);

        return ResponseEntity.ok(findUser);
    }
    /* 회원정보 삭제 */ //OK
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(id);
    }
    @GetMapping("/user/username")
    public ResponseEntity<UserDto> userOrAdmin(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        try {
            if (myUserDetails != null) {
                String username = myUserDetails.getUsername();
                Optional<User> user = userService.findByUsername(username);
                UserDto userDto = user.get().UsertoUserDto();
                if (user.isPresent()) {
                    return ResponseEntity.ok(userDto);
                } else {
                    log.error("User not found: {}", username);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                log.info("User not authenticated.");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Error while fetching username", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/user-id")
    public ResponseEntity<UserDto> userOrAdminById(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        try {
            if (myUserDetails != null) {
                Long userId = myUserDetails.getUserId(); // MyUserDetails에 추가한 getUserId 메서드를 사용하여 사용자의 ID를 가져옴
                Optional<User> user = userService.findById(userId);
                UserDto userDto = user.map(User::UsertoUserDto).orElse(null);
                if (user.isPresent()) {
                    return ResponseEntity.ok(userDto);
                } else {
                    log.error("User not found: {}", userId);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                log.info("User not authenticated.");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Error while fetching user information", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/login-status")
    public ResponseEntity<Boolean> isLogged() {
        // 현재 사용자의 인증 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 인증되어 있는지 확인
        boolean isAuthenticated = authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);

        return ResponseEntity.ok(isAuthenticated);
    }

    /* 회원가입 */
    @PostMapping("/user")
    public ResponseEntity<UserDto> join(@RequestBody UserJoinDto dto) {
        Long userId = registerMemberService.join(dto.getUsername(), dto.getPassword(), dto.getHakbun(), dto.getEmail());
        UserDto userdto = userService.read(userId);
        return ResponseEntity.ok(userdto);
    }

    /* 전체 유저 정보 리스트 조회 *///OK
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시글 저장 성공",
            content = @Content(schema = @Schema(implementation = PostsDto.Response.class))),
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"), @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")})
    @GetMapping
    public ResponseEntity<?> readFullofUser() {
        try {
            List<UserDto.Response> userList = userService.findFull();
            if (userList == null)
                log.info("조회된 게시글이 없음");
            else
                log.info("게시글 수 : " + userList.size());

            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (Exception e) {
            log.error(null, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/user/{password}")//OK
//    public ResponseEntity<UserDto> update(@AuthenticationPrincipal Long id, @RequestParam String password) {
//        userService.passwordUpdate(id, dto);
//        return ResponseEntity.ok(UserDto);
//    }

//        /* 회원정보 수정 */
//    @PutMapping("/user/modify")
//    public ResponseEntity<UserDto> modify( UserDto.Response user, Model model) {
//        if (user != null) {
//            model.addAttribute("user", user);
//        }
//        return "/user/user-modify";
//    }
//    @PutMapping("/{id}")//OK
//    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody PostsDto.Request dto) {
//        postsService.update(id, dto);
//        return ResponseEntity.ok(id);
//    }
//
//    @PostMapping("/login-process")
//    public String loginProcess(@RequestParam String userid, @RequestParam String pw) {
//        try {
//            Boolean login = userService.login(userid, pw);
//            if (login) {
//                log.info("login");
//                return "redirect:/view/dashboard";
//            }
//        } catch (Exception e) {
//            log.error("Login failed", e);
//        }
//        return "redirect:/view/login?error";
//    }

}
//login(String userName, String userPwd)