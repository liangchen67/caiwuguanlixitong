package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.Company;
import com.finance.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {
    
    @Autowired
    private CompanyService companyService;

    @GetMapping("/list")
    public Result<List<Company>> list() {
        return Result.success(companyService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Company> getById(@PathVariable Long id) {
        Company company = companyService.findById(id);
        if (company != null) {
            return Result.success(company);
        }
        return Result.error("企业不存在");
    }

    @PostMapping("/save")
    public Result<Company> save(@RequestBody Company company) {
        try {
            Company saved = companyService.save(company);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            companyService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}







