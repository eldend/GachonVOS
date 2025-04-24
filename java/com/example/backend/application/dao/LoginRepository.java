package com.example.backend.application.dao;

import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource
public interface LoginRepository extends JpaRepository<User, Long> {
//    @Modifying
//    @Query("select m from User m join fetch m.groups g where m.id = :sabun")
//    Optional<User> findUserByUsername(@Param("id") Long id);
        Optional<User> findUserByUsername(String username);
}
