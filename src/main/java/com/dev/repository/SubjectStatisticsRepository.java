package com.dev.repository;

import com.dev.models.SubjectStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectStatisticsRepository extends JpaRepository<SubjectStatistics, Long> {
    List<SubjectStatistics> findByUsername(String username);
    Optional<SubjectStatistics> findByUsernameAndSubjectType(String username, String subjectType);
}

