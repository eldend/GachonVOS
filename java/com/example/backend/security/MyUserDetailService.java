package com.example.backend.security;

import com.example.backend.application.dao.UserRepository;
import com.example.backend.application.dto.UserDto;
import com.example.backend.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpSession;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RequiredArgsConstructor
@Component
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + username));

        // 시큐리티 컨텍스트에 사용자 정보 설정
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        user.getAuthorities() // 사용자의 권한 정보를 설정하려면 해당 메서드를 사용
                )
        );
        session.setAttribute("user", new UserDto.Response(user));

        // 사용자 정보를 담은 MyUserDetails 반환
        return new MyUserDetails(user);
    }
}
//@Slf4j
//@Component
//public class MyUserDetailService implements UserDetailsService {
//
//    private final Logger logger = LoggerFactory.getLogger(MyUserDetailService.class);
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
//        logger.info("userId === "+userId);
//        Optional<User> userOptional = userRepository.findByUsername(userId);
//
//        if (!userOptional.isPresent()) {
//            throw new UsernameNotFoundException("해당 유저가 없습니다.");
//        }
//
//        User user = userOptional.get(); // Optional이 비어있지 않다면 값을 가져옴
//        ExamUser examUser = new ExamUser(user, buildAdminAuthority());
//
//        return examUser;
//    }
//
//    private Set<GrantedAuthority> buildAdminAuthority() {	//권한 부여 로직
//
//        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
//
//        setAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
//
//        return setAuths;
//    }
//}