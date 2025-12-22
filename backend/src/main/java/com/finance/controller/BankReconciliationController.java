package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.BankReconciliation;
import com.finance.service.BankReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bank-reconciliation")
public class BankReconciliationController {

    @Autowired
    private BankReconciliationService reconciliationService;

    @GetMapping("/list")
    public Result<List<BankReconciliation>> list() {
        return Result.success(reconciliationService.findAll());
    }

    @GetMapping("/{id}")
    public Result<BankReconciliation> getById(@PathVariable Long id) {
        BankReconciliation reconciliation = reconciliationService.findById(id);
        if (reconciliation != null) {
            return Result.success(reconciliation);
        }
        return Result.error("对账记录不存在");
    }

    @GetMapping("/account/{bankAccount}")
    public Result<List<BankReconciliation>> getByAccount(@PathVariable String bankAccount) {
        return Result.success(reconciliationService.findByBankAccount(bankAccount));
    }

    @PostMapping("/generate")
    public Result<Map<String, Object>> generateReport(
            @RequestParam String bankAccount,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reconciliationDate,
            @RequestParam BigDecimal bankBalance,
            @RequestParam(required = false) String createdBy) {
        try {
            Map<String, Object> result = reconciliationService.generateReconciliationReport(
                bankAccount, reconciliationDate, bankBalance, createdBy);
            return Result.success("生成成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 自动对账
     */
    @PostMapping("/auto-match")
    public Result<Map<String, Object>> autoMatch(@RequestBody Map<String, Object> params) {
        try {
            String bankAccount = (String) params.get("bankAccount");
            String startDateStr = (String) params.get("startDate");
            String endDateStr = (String) params.get("endDate");
            
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            
            Map<String, Object> result = reconciliationService.autoMatch(bankAccount, startDate, endDate);
            return Result.success("自动对账完成", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("自动对账失败：" + e.getMessage());
        }
    }

    /**
     * 生成余额调节表（使用@RequestBody）
     */
    @PostMapping("/generate-report")
    public Result<Map<String, Object>> generateReportByBody(@RequestBody Map<String, Object> params) {
        try {
            String bankAccount = (String) params.get("bankAccount");
            String reconciliationDateStr = (String) params.get("reconciliationDate");
            BigDecimal bankBalance = new BigDecimal(params.get("bankBalance").toString());
            String createdBy = (String) params.get("createdBy");
            
            LocalDate reconciliationDate = LocalDate.parse(reconciliationDateStr);
            
            Map<String, Object> result = reconciliationService.generateReconciliationReport(
                bankAccount, reconciliationDate, bankBalance, createdBy);
            return Result.success("生成成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("生成失败：" + e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getDetail(@PathVariable Long id) {
        try {
            Map<String, Object> detail = reconciliationService.getReconciliationDetail(id);
            return Result.success(detail);
        } catch (Exception e) {
            return Result.error("获取详情失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            reconciliationService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}

