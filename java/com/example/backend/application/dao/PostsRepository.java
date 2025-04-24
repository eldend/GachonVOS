package com.example.backend.application.dao;

import com.example.backend.domain.PostCategory;
import com.example.backend.domain.Posts;
import com.example.backend.domain.ProcessState;
import com.example.backend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Modifying
    @Query("update Posts p set p.view = p.view + 1 where p.id = :id")
    int updateView(Long id);

    Page<Posts> findByTitleContaining(String keyword, Pageable pageable);

    List<Posts> findAllByUserId(Long id);
    //    List<Posts> findAllByIdIn(List<Long> postIds); //추가. 브리핑에서 id로 찾는거 임시로 사용했음.
    List<Posts> findByProcessState(ProcessState processState); //추가. 브리핑에서 process_state로 title찾으려고 사용, Enum 타입으로 받아야됨

    int countByProcessState(ProcessState processState);    // ProcessState(처리상태)에 따른 미처리된 건의글의 개수를 가져오는 메서드

    int countByPostCategoryAndProcessState(PostCategory category, ProcessState state);  //'처리 대기'상태인 건의글만 PostCategory(건의글카테고리)에 따라 카테고리별 건의글 개수를 가져오는 메서드
    // ProcessState에 따른 미처리된 건의글의 개수를 가져오는 메서드
}