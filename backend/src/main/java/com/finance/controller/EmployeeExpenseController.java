package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.EmployeeExpense;
import com.finance.service.EmployeeExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employee-expense")
public class EmployeeExpenseController {
    
    @Autowired
    private EmployeeExpenseService employeeExpenseService;

    @GetMapping("/list")
    public Result<List<EmployeeExpense>> list() {
        return Result.success(employeeExpenseService.findAll());
    }

    @GetMapping("/{id}")
    public Result<EmployeeExpense> getById(@PathVariable Long id) {
        EmployeeExpense expense = employeeExpenseService.findById(id);
        if (expense != null) {
            return Result.success(expense);
        }
        return Result.error("员工费用不存在");
    }

    @GetMapping("/status/{status}")
    public Result<List<EmployeeExpense>> getByStatus(@PathVariable String status) {
        return Result.success(employeeExpenseService.findByStatus(status));
    }

    @GetMapping("/employee/{employeeId}")
    public Result<List<EmployeeExpense>> getByEmployeeId(@PathVariable Long employeeId) {
        return Result.success(employeeExpenseService.findByEmployeeId(employeeId));
    }

    @PostMapping("/save")
    public Result<EmployeeExpense> save(@RequestBody EmployeeExpense expense) {
        try {
            EmployeeExpense saved = employeeExpenseService.save(expense);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            employeeExpenseService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}







