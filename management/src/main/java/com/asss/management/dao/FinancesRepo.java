package com.asss.management.dao;

import com.asss.management.entity.ExamStatusInfo;
import com.asss.management.entity.Finances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancesRepo extends JpaRepository<Finances, Integer> {

    @Query("SELECT f FROM Finances f WHERE f.student.email= ?1")
    List<Finances> financesForStudent(String studentEmail);

    @Query("SELECT f FROM Finances f WHERE f.student.email= ?1 AND f.idExam.id = ?2 ")
    List<Finances> financesForSpecificExamEntry(String studentEmail, Integer examID);
}
