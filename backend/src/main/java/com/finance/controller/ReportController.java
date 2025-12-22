package com.finance.controller;

import com.finance.common.Result;
import com.finance.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

/**
 * 财务报表控制器
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取资产负债表
     */
    @GetMapping("/balance-sheet")
    public Result<Map<String, Object>> getBalanceSheet(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            Map<String, Object> report = reportService.generateBalanceSheet(endDate);
            return Result.success(report);
        } catch (Exception e) {
            return Result.error("生成资产负债表失败：" + e.getMessage());
        }
    }

    /**
     * 获取利润表
     */
    @GetMapping("/income-statement")
    public Result<Map<String, Object>> getIncomeStatement(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            if (startDate == null) {
                startDate = LocalDate.of(endDate.getYear(), 1, 1); // 默认本年初
            }
            Map<String, Object> report = reportService.generateIncomeStatement(startDate, endDate);
            return Result.success(report);
        } catch (Exception e) {
            return Result.error("生成利润表失败：" + e.getMessage());
        }
    }

    /**
     * 获取现金流量表
     */
    @GetMapping("/cash-flow")
    public Result<Map<String, Object>> getCashFlow(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            if (startDate == null) {
                startDate = LocalDate.of(endDate.getYear(), 1, 1); // 默认本年初
            }
            Map<String, Object> report = reportService.generateCashFlow(startDate, endDate);
            return Result.success(report);
        } catch (Exception e) {
            return Result.error("生成现金流量表失败：" + e.getMessage());
        }
    }
}







