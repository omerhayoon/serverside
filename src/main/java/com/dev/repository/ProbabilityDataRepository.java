package com.dev.repository;

import com.dev.models.ProbabilityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProbabilityDataRepository extends JpaRepository<ProbabilityData, Long> {
    List<ProbabilityData> findByType(String type);
}