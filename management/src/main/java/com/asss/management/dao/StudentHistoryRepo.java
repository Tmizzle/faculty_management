package com.asss.management.dao;

import com.asss.management.entity.StudentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentHistoryRepo extends JpaRepository<StudentHistory, Integer> {

    @Query("SELECT s FROM StudentHistory s WHERE s.student.index= ?1 ")
    List<StudentHistory> studentHistoryByIndex(String index);
}
