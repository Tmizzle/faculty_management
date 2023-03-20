package com.asss.management.dao;

import com.asss.management.entity.Exams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamsRepo extends JpaRepository<Exams, Integer> {
}
