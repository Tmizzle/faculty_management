package com.asss.management.dao;

import com.asss.management.entity.Exams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExamsRepo extends JpaRepository<Exams, Integer> {

    @Query("SELECT e FROM Exams e WHERE e.profesor.email= ?1 AND e.event.idExamPeriod.id= ?2")
    List<Exams> findExamsForEventByProfesor(String profesorEmail, Integer eventID);

    @Query("SELECT e FROM Exams e WHERE e.event.idExamPeriod.id= ?1")
    List<Exams> findByEvent(Integer eventID);

    @Query("SELECT e FROM Exams e WHERE e.subject.id= ?1 AND e.event.idExamPeriod.id= ?2 AND e.student.index = ?3")
    Exams findIfStudentRegistratedTheExam(Integer subjectID, Integer eventID, String index);

    @Query("SELECT e FROM Exams e WHERE e.subject.id= ?1 AND e.event.idExamPeriod.id= ?2 AND e.student.email = ?3")
    Exams findIfEntryExists(Integer subjectID, Integer eventID, String email);

    @Query("SELECT e FROM Exams e WHERE e.student.email= ?1 AND e.event.idExamPeriod.id = ?2")
    List<Exams> findByStudentForEvent(String studentEmail, Integer eventID);

    @Query("SELECT e FROM Exams e WHERE e.subject.id= ?1 AND e.event.idExamPeriod.id = ?2")
    List<Exams> findBySubjectForEvent(Integer subjectID, Integer eventID);

    @Query("SELECT e FROM Exams e WHERE e.subject.id= ?1 AND e.student.email= ?2 AND ?3 BETWEEN e.event.startDate AND e.event.endDate")
    Exams checkIfExamAlreadyRegisteredForEventOngoing(Integer subjectID, String stundetEmail, Date currentTime);

    @Query("SELECT e FROM Exams e WHERE e.subject.id= ?1 AND e.student.email= ?2")
    List<Exams> findHowManyTimesDidStudentRegisterAnExam(Integer subjectID, String studentEmail);
}
