package com.dev.repository;

import com.dev.models.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLevelRepository extends JpaRepository<UserLevel, Long> {

    Optional<UserLevel> findByUsernameAndSubjectType(String username, String subjectType);

    void deleteByUsername(String username);
}