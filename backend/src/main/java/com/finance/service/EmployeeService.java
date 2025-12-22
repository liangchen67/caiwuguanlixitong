package com.finance.service;

import com.finance.entity.Employee;
import com.finance.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> findByStatus(String status) {
        return employeeRepository.findByStatus(status);
    }

    public List<Employee> searchByName(String name) {
        return employeeRepository.findByNameContaining(name);
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}







