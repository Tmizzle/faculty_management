package com.asss.management.dao;

import com.asss.management.entity.Employee;
import com.asss.management.entity.Enums.Employee_category;
import com.asss.management.service.dto.EmployeeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e WHERE e.email= ?1 AND e.password = ?2")
    Employee loginEmployeeParams(String email, String password);

    @Query("SELECT e FROM Employee e WHERE e.email= ?1")
    Employee findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.email= ?1")
    Optional<Employee> findByEmailSecurity(String email);

    @Query("SELECT e FROM Employee e WHERE e.jmbg= ?1")
    Employee findByJMBG(Integer jmbg);

    @Query("SELECT e FROM Employee e WHERE e.employeeCategory= ?1")
    List<Employee> findProfessors(Employee_category category);
}
