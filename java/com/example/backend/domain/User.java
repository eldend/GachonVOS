package com.example.backend.domain;

import com.example.backend.application.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class User extends BaseTimeEntity{

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username; // 아이디

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true)
    private int hakbun;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /* 회원정보 수정 */
    public void modify(int hakbun, String password) {
        this.hakbun = hakbun;
        this.password = password;
    }
    public User (String username, String password, int hakbun,
                                  String email) {
        this.username = username;
        this.password = password;
        this.hakbun = hakbun;
        this.email = email;
    }

    public static User createUser(String username, String password, int hakbun,
                                  String email, PasswordEncoder passwordEncoder) {
        return new User(null, username, passwordEncoder.encode(password), hakbun, email, Role.USER);
    }

    public UserDto UsertoUserDto() {
        User user = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .hakbun(hakbun)
                .email(email)
                .role(role)
                .build();
        return new UserDto(user);
    }

    /* 소셜로그인시 이미 등록된 회원이라면 수정날짜만 업데이트해줘서
     * 기존 데이터를 보존하도록 예외처리 */
    public User updateModifiedDate() {
        this.onPreUpdate();
        return this;
    }

    public User encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 정보 반환
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }



//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // 권한 정보 반환
//        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
//    }

}

