package com.asss.management.dao;

import com.asss.management.entity.PassedExams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassedExamsRepo extends JpaRepository<PassedExams, Integer> {

    @Query("SELECT p FROM PassedExams p WHERE p.subject.id= ?1 AND p.student.id = ?2")
    PassedExams checkIfExamAlreadyPassed(Integer subjectID, Integer studentID);

    @Query("SELECT p FROM PassedExams p WHERE p.profesor.email= ?1")
    List<PassedExams> passedExamsByProfesor(String profesorEmail);

    @Query("SELECT p FROM PassedExams p WHERE p.student.email= ?1")
    List<PassedExams> passedExamsByStudent(String studentEmail);
}
