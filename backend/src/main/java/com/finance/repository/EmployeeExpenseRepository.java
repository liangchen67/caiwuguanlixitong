package com.finance.repository;

import com.finance.entity.EmployeeExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeExpenseRepository extends JpaRepository<EmployeeExpense, Long> {
    Optional<EmployeeExpense> findByExpenseNo(String expenseNo);
    List<EmployeeExpense> findByStatus(String status);
    List<EmployeeExpense> findByEmployeeId(Long employeeId);
    List<EmployeeExpense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);
}







