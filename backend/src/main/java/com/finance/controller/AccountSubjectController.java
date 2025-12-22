package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.AccountSubject;
import com.finance.service.AccountSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/account-subject")
public class AccountSubjectController {
    
    @Autowired
    private AccountSubjectService accountSubjectService;

    @GetMapping("/list")
    public Result<List<AccountSubject>> list() {
        return Result.success(accountSubjectService.findAll());
    }

    @GetMapping("/{id}")
    public Result<AccountSubject> getById(@PathVariable Long id) {
        AccountSubject subject = accountSubjectService.findById(id);
        if (subject != null) {
            return Result.success(subject);
        }
        return Result.error("会计科目不存在");
    }

    @GetMapping("/code/{code}")
    public Result<AccountSubject> getByCode(@PathVariable String code) {
        AccountSubject subject = accountSubjectService.findByCode(code);
        if (subject != null) {
            return Result.success(subject);
        }
        return Result.error("会计科目不存在");
    }

    @GetMapping("/type/{type}")
    public Result<List<AccountSubject>> getByType(@PathVariable String type) {
        return Result.success(accountSubjectService.findByType(type));
    }

    @GetMapping("/parent/{parentId}")
    public Result<List<AccountSubject>> getByParentId(@PathVariable Long parentId) {
        return Result.success(accountSubjectService.findByParentId(parentId));
    }

    @GetMapping("/enabled")
    public Result<List<AccountSubject>> getEnabled() {
        return Result.success(accountSubjectService.findEnabled());
    }

    @PostMapping("/save")
    public Result<AccountSubject> save(@RequestBody AccountSubject accountSubject) {
        try {
            AccountSubject saved = accountSubjectService.save(accountSubject);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            accountSubjectService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}







