package com.asss.management.dao;

import com.asss.management.entity.PassedExams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PassedExamsRepo extends JpaRepository<PassedExams, Integer> {

    @Query("SELECT p FROM PassedExams p WHERE p.subject.id= ?1 AND p.student.id = ?2")
    PassedExams checkIfExamAlreadyPassed(Integer subjectID, Integer studentID);
}
