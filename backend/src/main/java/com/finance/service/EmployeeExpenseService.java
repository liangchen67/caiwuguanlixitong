package com.finance.service;

import com.finance.entity.EmployeeExpense;
import com.finance.repository.EmployeeExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmployeeExpenseService {
    
    @Autowired
    private EmployeeExpenseRepository employeeExpenseRepository;

    public List<EmployeeExpense> findAll() {
        return employeeExpenseRepository.findAll();
    }

    public EmployeeExpense findById(Long id) {
        return employeeExpenseRepository.findById(id).orElse(null);
    }

    public List<EmployeeExpense> findByStatus(String status) {
        return employeeExpenseRepository.findByStatus(status);
    }

    public List<EmployeeExpense> findByEmployeeId(Long employeeId) {
        return employeeExpenseRepository.findByEmployeeId(employeeId);
    }

    public long count() {
        return employeeExpenseRepository.count();
    }

    @Transactional
    public EmployeeExpense save(EmployeeExpense expense) {
        // 如果是新增，生成费用单号
        if (expense.getId() == null) {
            expense.setExpenseNo(generateExpenseNo());
        }
        return employeeExpenseRepository.save(expense);
    }

    @Transactional
    public void delete(Long id) {
        employeeExpenseRepository.deleteById(id);
    }

    private String generateExpenseNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = employeeExpenseRepository.count() + 1;
        return String.format("EE-%s-%04d", date, count);
    }
}

