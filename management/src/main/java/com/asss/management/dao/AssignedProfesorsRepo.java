package com.asss.management.dao;

import com.asss.management.entity.AssignedProfesors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignedProfesorsRepo extends JpaRepository<AssignedProfesors, Integer> {

    @Query("SELECT a FROM AssignedProfesors a WHERE a.profesor.email= ?1")
    List<AssignedProfesors> findByProfesor(String profesorEmail);

    @Query("SELECT a FROM AssignedProfesors a WHERE a.subject.id= ?1")
    List<AssignedProfesors> findBySubject(Integer subjectID);

    @Query("SELECT a FROM AssignedProfesors a WHERE a.profesor.id= ?1 AND a.subject.id= ?2")
    AssignedProfesors findIfAlreadyAssigned(Integer profesorID, Integer subjectID);
}
