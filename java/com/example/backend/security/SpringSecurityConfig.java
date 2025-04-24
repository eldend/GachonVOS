package com.example.backend.security;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.apache.commons.lang3.BooleanUtils.and;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근하면 권한 및 인증을 미리 체크
@EnableMethodSecurity
public class SpringSecurityConfig {

    private final AuthenticationFailureHandler customFailureHandler;
    private final MyUserDetailService myUserDetailService;
    private final LoginSuccessHandler loginSuccessHandler;
//    @Bean
//    public HttpFirewall defaultHttpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowUrlEncodedDoubleSlash(true);//임시
//        return firewall;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf().disable()
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/signup", "/userlogin", "/adminlogin", "/auth/join", "/api/**", "/logout").permitAll()
                        //"/wirte" 뺌, "/images/**"뺌
                        .anyRequest().authenticated()
                )

                .formLogin(login -> login
                        .loginPage("/userlogin").permitAll()
                        .loginProcessingUrl("/login-proc")
                        .successHandler( // 로그인 성공 후 핸들러
                                new AuthenticationSuccessHandler() { // 익명 객체 사용
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        System.out.println("authentication: " + authentication.getName());
                                        response.sendRedirect("/");
                                    }
                                })
                        .failureHandler(customFailureHandler)
                        .usernameParameter("userid")
                        .passwordParameter("pw")
                        .defaultSuccessUrl("http://localhost:3000/vos", true)

                        .permitAll()
                )
                .sessionManagement(s -> s
                        .maximumSessions(1) // 동시에 한 유저당 한 세션만 허용 됨
                        // default인 false는 기존의 세션을 만료시키는 것
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/session-expired"))

                .logout(out->out
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //로그아웃 처리 URL 지정
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);

                        })
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID")// 로그아웃 시 세션을 무효화, 지정된 쿠키 삭제
                        .logoutSuccessUrl("/userlogin")) // 로그아웃 성공 시 이동할 URL 지정
                .userDetailsService(myUserDetailService);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailService);
        // 다른 설정들을 추가할 수 있음
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000"); // 클라이언트의 주소
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        source.registerCorsConfiguration("/logout", configuration);
        source.registerCorsConfiguration("/userlogin", configuration);
        return source;
    }
}

