package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.BankStatement;
import com.finance.service.BankStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bank-statement")
public class BankStatementController {

    @Autowired
    private BankStatementService bankStatementService;

    @GetMapping("/list")
    public Result<List<BankStatement>> list() {
        return Result.success(bankStatementService.findAll());
    }

    @GetMapping("/{id}")
    public Result<BankStatement> getById(@PathVariable Long id) {
        BankStatement statement = bankStatementService.findById(id);
        if (statement != null) {
            return Result.success(statement);
        }
        return Result.error("银行流水不存在");
    }

    @GetMapping("/account/{bankAccount}")
    public Result<List<BankStatement>> getByAccount(@PathVariable String bankAccount) {
        return Result.success(bankStatementService.findByBankAccount(bankAccount));
    }

    @GetMapping("/date-range")
    public Result<List<BankStatement>> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(bankStatementService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/unreconciled")
    public Result<List<BankStatement>> getUnreconciled(
            @RequestParam(required = false) String bankAccount) {
        return Result.success(bankStatementService.findUnreconciled(bankAccount));
    }

    @PostMapping("/save")
    public Result<BankStatement> save(@RequestBody BankStatement statement) {
        try {
            BankStatement saved = bankStatementService.save(statement);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @PostMapping("/batch-save")
    public Result<List<BankStatement>> batchSave(@RequestBody List<BankStatement> statements) {
        try {
            List<BankStatement> saved = bankStatementService.batchSave(statements);
            return Result.success("批量保存成功", saved);
        } catch (Exception e) {
            return Result.error("批量保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            bankStatementService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @PostMapping("/auto-match")
    public Result<Map<String, Object>> autoMatch(
            @RequestParam String bankAccount,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            Map<String, Object> result = bankStatementService.autoMatch(bankAccount, startDate, endDate);
            return Result.success("自动匹配完成", result);
        } catch (Exception e) {
            return Result.error("自动匹配失败：" + e.getMessage());
        }
    }

    @PostMapping("/manual-match")
    public Result<Void> manualMatch(
            @RequestParam Long statementId,
            @RequestParam Long journalEntryId) {
        try {
            bankStatementService.manualMatch(statementId, journalEntryId);
            return Result.success("手动匹配成功", null);
        } catch (Exception e) {
            return Result.error("手动匹配失败：" + e.getMessage());
        }
    }

    @PostMapping("/unmatch/{id}")
    public Result<Void> unmatch(@PathVariable Long id) {
        try {
            bankStatementService.unmatch(id);
            return Result.success("取消匹配成功", null);
        } catch (Exception e) {
            return Result.error("取消匹配失败：" + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(
            @RequestParam(required = false) String bankAccount) {
        try {
            Map<String, Object> stats = bankStatementService.getStatistics(bankAccount);
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取统计信息失败：" + e.getMessage());
        }
    }
}










