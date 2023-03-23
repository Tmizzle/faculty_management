package com.asss.management.dao;

import com.asss.management.entity.Employee;
import com.asss.management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student s WHERE s.email= ?1 AND s.password = ?2")
    Student loginStudentParams(String email, String password);

    @Query("SELECT s FROM Student s WHERE s.email= ?1")
    Student findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.email= ?1")
    Optional<Student> findByEmailSecurity(String email);

    @Query("SELECT s FROM Student s WHERE s.jmbg= ?1")
    Student findByJMBG(Integer jmbg);

    @Query("SELECT s FROM Student s WHERE s.index= ?1")
    Student findByIndex(String index);
}
