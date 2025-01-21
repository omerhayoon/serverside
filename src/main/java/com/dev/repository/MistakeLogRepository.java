package com.dev.repository;

import com.dev.models.MistakeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MistakeLogRepository extends JpaRepository<MistakeLog, Long> {
    List<MistakeLog> findByUsernameAndResolvedFalse(String username);
    List<MistakeLog> findByUsernameAndSubjectType(String username, String subjectType);
    List<MistakeLog> findByUsernameAndSubjectTypeAndResolvedFalse(String username, String subjectType);
}
