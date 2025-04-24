//package com.example.backend.application.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//@Service
//public class AuthenticationService {
//
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    public AuthenticationService(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    public String login(String userName, String userPwd) {
//        // 사용자 인증
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userName, userPwd)
//        );
//
//        // 인증 정보를 SecurityContextHolder에 저장
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // 로그인 성공 시 UserDetails를 반환받아 추가 작업 가능
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        // 로그인 성공시 추가 작업 수행
//        // 예: 로그인 이력 기록, 사용자 정보 갱신 등
//
//        return "로그인 성공!";
//    }
//}
//
