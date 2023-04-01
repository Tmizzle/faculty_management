package com.asss.management.dao;

import com.asss.management.entity.Events;
import com.asss.management.entity.ExamStatusInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExamStatusInfoRepo extends JpaRepository<ExamStatusInfo, Integer> {

    @Query("SELECT e FROM ExamStatusInfo e WHERE e.subject.id= ?1 AND e.student.index = ?2")
    ExamStatusInfo findExamInfoForSubject(Integer subjectID, String index);

    @Query("SELECT e FROM ExamStatusInfo e WHERE e.profesor.email= ?1")
    List<ExamStatusInfo> passedExamsByProfesor(String profesorEmail);

    @Query("SELECT e FROM ExamStatusInfo e WHERE e.student.email= ?1 AND e.status = 'PASSED'")
    List<ExamStatusInfo> passedExamsByStudent(String studentEmail);

    @Query("SELECT e FROM ExamStatusInfo e WHERE e.student.email= ?1 AND e.status = 'UNPASSED'")
    List<ExamStatusInfo> unpassedExamsByStudent(String studentEmail);

}
