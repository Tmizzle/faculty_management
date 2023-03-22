package com.asss.management.dao;

import com.asss.management.entity.AssignedProfesors;
import com.asss.management.entity.Exams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamsRepo extends JpaRepository<Exams, Integer> {

    @Query("SELECT e FROM Exams e WHERE e.profesor.email= ?1 AND e.event.id= ?2")
    List<Exams> findExamsForEventByProfesor(String profesorEmail, Integer eventID);

    @Query("SELECT e FROM Exams e WHERE e.event.id= ?1")
    List<Exams> findByEvent(Integer eventID);

    @Query("SELECT e FROM Exams e WHERE e.student.email= ?1 AND e.event.id= ?2")
    List<Exams> findByStudentForEvent(String studentEmail, Integer eventID);

    @Query("SELECT e FROM Exams e WHERE e.subject.id= ?1 AND e.event.id= ?2")
    List<Exams> findBySubjectForEvent(Integer subjectID, Integer eventID);

}
