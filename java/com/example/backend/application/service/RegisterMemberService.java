package com.example.backend.application.service;

import com.example.backend.domain.Role;
import com.example.backend.domain.User;
import com.example.backend.application.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterMemberService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    @Autowired
    public RegisterMemberService(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    public Long join(String username, String password, int hakbun, String email) {
        User user = User.createUser(username, password, hakbun, email, passwordEncoder);
        validateDuplicateUser(user);
        repository.save(user);

        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        repository.findByUsername(user.getUsername())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다. (아이디)");
                });
        repository.findByUsername(user.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다. (이메일)");
                });
        if (repository.existsByHakbun(user.getHakbun())) {
            throw new IllegalStateException("이미 존재하는 회원입니다. (학번)");
        }
    }
}
