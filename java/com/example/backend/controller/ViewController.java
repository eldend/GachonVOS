package com.example.backend.controller;

import com.example.backend.config.AdminAuthorize;
import com.example.backend.config.UserAuthorize;
import com.example.backend.security.MyUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/view")
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

//    @GetMapping("/dashboard")
//    public String dashboardPage(@AuthenticationPrincipal User user, Model model) {
//        if (user != null) {
//            model.addAttribute("loginId", user.getUsername());
//            model.addAttribute("loginRoles", user.getAuthorities());
//            return "dashboard";
//        } else {
//            // 처리할 로직 또는 에러 핸들링 추가
//            return "redirect:/view/login"; // 또는 다른 페이지로 리다이렉트 또는 에러 페이지 표시 등
//        }
//    }

    @GetMapping("/dashboard")
    public String dashboardPage(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("loginId", userDetails.getUsername());
            model.addAttribute("loginRoles", userDetails.getAuthorities());
            log.info("userDetails.getUsername()"+ userDetails.getUsername());
            log.info("userDetails.getAuthorities()"+ userDetails.getAuthorities());
            return "/dashboard";
        } else {
            log.info("User not authenticated. Redirecting to /view/login");
            return "redirect:/view/login"; // 또는 다른 페이지로 리다이렉트 또는 에러 페이지 표시 등
        }
    }

    @GetMapping("/setting/admin")
    @AdminAuthorize
    public String adminSettingPage() {
        return "admin_setting";
    }

    @GetMapping("/setting/user")
    @UserAuthorize
    public String userSettingPage() {
        return "user_setting";
    }///whologged

    @GetMapping("/whologged")
    public String userPage(@AuthenticationPrincipal MyUserDetails userDetails) {
        if (userDetails != null) {
            if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                // 사용자의 권한이 ADMIN인 경우
                return "redirect:http://localhost:3000/postmanage";
            } else if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
                // 사용자의 권한이 USER인 경우
                return "http://localhost:3000/vos";
            }
        }
        // 권한이 ADMIN 또는 USER가 아닌 경우 또는 userDetails가 null인 경우 기본 리다이렉트
        return "redirect:/default";
    }





//    @GetMapping("/whologged")
//    @AdminAuthorize
//    public String adminPage() {
//        return "redirect:/postmanage";
//    }
}