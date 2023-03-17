package com.asss.management.dao;

import com.asss.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e WHERE e.email= ?1 AND e.password = ?2")
    Employee loginEmployeeParams(String email, String password);

    @Query("SELECT e FROM Employee e WHERE e.email= ?1")
    Employee findByEmail(String email);
}
