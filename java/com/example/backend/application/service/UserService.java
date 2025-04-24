package com.example.backend.application.service;

import com.example.backend.application.dto.PostsDto;
import com.example.backend.application.dto.UserDto;
import com.example.backend.domain.Posts;
import com.example.backend.security.MyUserDetailService;
import com.example.backend.domain.User;
import com.example.backend.application.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;
    //private final AuthenticationManager authenticationManager;
    private final MyUserDetailService userDetailsService;
    //private final JwtTokenUtil jwtTokenUtil;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    /* 회원가입 */
    @Transactional
    public void userJoin(UserDto.Request dto) {
        dto.setPassword((dto.getPassword()));
        userRepository.save(dto.toEntity());
    }

    @Transactional(readOnly = true)
    public List<UserDto.Response> findFull() {
        List<User> users = userRepository.findAll();
        return UserDto.fromEntityList(users);
    }

    /* 회원가입 시, 유효성 검사 및 중복 체크 */
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사, 중복 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    /* 회원수정 (dirty checking) */
    @Transactional
    public void modify(UserDto.Request dto) {
        User user = userRepository.findById(dto.toEntity().getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        String encPassword = dto.getPassword();
        user.modify(dto.getHakbun(), encPassword);
    }

//    @Transactional
//    public boolean login(String userName, String userPwd) {
//        Optional<User> user = userRepository.findByUsername(userName);
//        if (user.isPresent() && encoder.matches(userPwd, user.get().getPassword())) {
//            log.info("Entered Password: {}", userPwd);
//            log.info("Encoded Password: {}", user.get().getPassword());
//            log.info("Password Matched: Login successful");
//            return true;
//        }else {
//            log.info("Entered Password: {}", userPwd);
//            log.info("Encoded Password: {}", user.map(User::getPassword).orElse("No user found"));
//            log.info("Password Mismatched: Login failed");
//            return false;
//        }
//    }

    public UserDto read(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.get().UsertoUserDto();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

//    public UserDto passwordupdate(Long id, UserDto.Request dto) {
//        User user = userRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + id));
//        if(dto.getPassword() == )
//        user.update(encoder.encode(dto.getPassword()));
//    }

//    @Transactional
//    public void update(Long id, PostsDto.Request dto) {
//        Posts posts = postsRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
//
//        posts.update(dto.getTitle(), dto.getContent());
//    }

//public UserDto login(UserDto loginDto) {
//    authenticate(loginDto.getEmail(), loginDto.getPassword());
//    UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
//    checkEncodePassword(loginDto.getPassword(), userDetails.getPassword());
//    String token = jwtTokenUtil.generateToken(userDetails);
//    return MemberTokenDto.fromEntity(userDetails, token);
//}
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::new).collect(Collectors.toList());
}
}
