package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.Employee;
import com.finance.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public Result<List<Employee>> list() {
        return Result.success(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        if (employee != null) {
            return Result.success(employee);
        }
        return Result.error("员工不存在");
    }

    @GetMapping("/status/{status}")
    public Result<List<Employee>> getByStatus(@PathVariable String status) {
        return Result.success(employeeService.findByStatus(status));
    }

    @GetMapping("/search")
    public Result<List<Employee>> search(@RequestParam String name) {
        return Result.success(employeeService.searchByName(name));
    }

    @PostMapping("/save")
    public Result<Employee> save(@RequestBody Employee employee) {
        try {
            Employee saved = employeeService.save(employee);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            employeeService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}










