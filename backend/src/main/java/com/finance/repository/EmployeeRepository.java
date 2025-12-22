package com.finance.repository;

import com.finance.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeNo(String employeeNo);
    List<Employee> findByStatus(String status);
    List<Employee> findByDepartment(String department);
    List<Employee> findByNameContaining(String name);
}







