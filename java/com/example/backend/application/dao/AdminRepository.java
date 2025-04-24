package com.example.backend.application.dao;

import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<User, Long> {
    /* Security */
    Optional<User> findByUsername(String username);

    /* OAuth */
    Optional<User> findByEmail(String email);

    /* user GET */
    User findByHakbun(int hakbun);

    /* 중복 검사> 중복인 경우 true, 중복되지 않은경우 false 리턴 */
    boolean existsByUsername(String username);
    boolean existsByHakbun(int hakbun);
    boolean existsByEmail(String email);
}
