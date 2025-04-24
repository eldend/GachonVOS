//package com.example.backend.config;
//
//import jakarta.servlet.DispatcherType;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
///**
// * Security 설정 클래스
// */
//@RequiredArgsConstructor
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근하면 권한 및 인증을 미리 체크
//public class SecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManagerBean(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    /* static 관련설정은 무시 */
////    @Bean
////    public void configure(WebSecurity web) throws Exception {
////        web
////                .ignoring().antMatchers( "/css/**", "/js/**", "/img/**");
////    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**");
//    }
//
//    @Bean
//    protected SecurityFilterChain SecurityfilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable().cors().disable()
//                .authorizeHttpRequests(request -> request
//                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                        .requestMatchers("/status", "/images/**", "/view/join", "/auth/join").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login
//                        .loginPage("/view/login")
//                        .loginProcessingUrl("/login-process")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/view/dashboard", true)
//                        .permitAll()
//                )
//                .logout(withDefaults());
//
//        return http.build();
//
//
//    }
//}
//
////        http
////                .csrf().disable()
////                .cors(Customizer.withDefaults());
////                .authorizeHttpRequests((authorizeRequests) ->
////                        authorizeRequests
////                                .requestMatchers("/**")
////                )
////                .formLogin((formLogin) ->
////                        formLogin
////                                .loginPage("/auth/login")
////                                .defaultSuccessUrl("/posts/read", true)
////                                .permitAll()
////                );
////        return http.build();
//
//
////        http
////                .csrf().disable().cors().disable()
//////                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer
//////                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
////
////
////                .authorizeHttpRequests(request -> request
////                        .requestMatchers("/", "/auth/**", "/posts/read/**", "/posts/search/**").permitAll() //모든사용자 접근허용
////                        .anyRequest().authenticated() //나머지 모든 요청에는 인증된 사용자만 접근을 허용
////                )
////                .formLogin(login -> login//form 기반 로그인 설정
////                        .loginPage("/auth/login") //로그인 페이지 URL 설정)
////
////                        .loginProcessingUrl("/auth/loginProc")//실제 로그인 페이지 URL 지정
////                        .failureHandler(customFailureHandler) //로그인 실패 시 호출되는 핸들러 지정
////                        .defaultSuccessUrl("/") //로그인 성공 시 이동할 URL 지정
////                )
////                .logout(logout -> logout//로그아웃 관련 설정
////                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //로그아웃 처리 URL 지정
////                        .invalidateHttpSession(true).deleteCookies("JSESSIONID")// 로그아웃 시 세션을 무효화, 지정된 쿠키 삭제
////                        .logoutSuccessUrl("/")// 로그아웃 성공 시 이동할 URL 지정
////                )
////                .oauth2Login(oauth2 -> oauth2
////                        .userInfoEndpoint(userInfo -> userInfo
////                                .userService(customOAuth2UserService)
////                        )//OAuth2 로그인 설정
////                );
////                         // OAuth2 로그인 성공 후 가져올 정보의 설정들 지정
////                          // 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
////
//////                .oauth2Login() //OAuth2 로그인 설정
//////                .userInfoEndpoint() // OAuth2 로그인 성공 후 가져올 정보의 설정들 지정
//////                .userService(customOAuth2UserService); // 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
////        return http.build();
//
////        http.cors(cor -> {
////            cor.disable();})
////                    .authorizeHttpRequests(request -> {
////                        request
////                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
////                                .requestMatchers("/", "/auth/**", "/posts/read/**", "/posts/search/**",
////                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll();
////                                //.anyRequest().authenticated();
////                    })
//////                    .formLogin(login -> login
//////                            .loginPage("/auth/login")
//////                            .loginProcessingUrl("/auth/loginProc")
//////                            .usernameParameter("userid")
//////                            .passwordParameter("pw")
//////                            .defaultSuccessUrl("/posts/read", true)
//////                            .permitAll()
//////                    )
////                    .logout(withDefaults());
////
////        return http.build();
//
//
//
//
