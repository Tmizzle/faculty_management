package com.asss.management.dao;

import com.asss.management.entity.Student;
import com.asss.management.entity.Subjects;
import com.asss.management.service.dto.SubjectsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectsRepo extends JpaRepository<Subjects, Integer> {

    @Query("SELECT s FROM Subjects s WHERE s.year= ?1")
    List<SubjectsDTO> findByYear(String year);

    @Query("SELECT s FROM Subjects s WHERE s.semester= ?1")
    List<SubjectsDTO> findBySemester(String semester);

    @Query("SELECT s FROM Subjects s WHERE s.name= ?1")
    Subjects findByName(String name);
}
