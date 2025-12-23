package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.Customer;
import com.finance.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public Result<List<Customer>> list() {
        return Result.success(customerService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            return Result.success(customer);
        }
        return Result.error("客户不存在");
    }

    @GetMapping("/status/{status}")
    public Result<List<Customer>> getByStatus(@PathVariable String status) {
        return Result.success(customerService.findByStatus(status));
    }

    @GetMapping("/search")
    public Result<List<Customer>> search(@RequestParam String name) {
        return Result.success(customerService.searchByName(name));
    }

    @PostMapping("/save")
    public Result<Customer> save(@RequestBody Customer customer) {
        try {
            Customer saved = customerService.save(customer);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            customerService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}










