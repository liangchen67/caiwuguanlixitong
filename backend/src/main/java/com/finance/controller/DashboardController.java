package com.finance.controller;

import com.finance.common.Result;
import com.finance.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页统计数据控制器
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private SalesInvoiceService salesInvoiceService;

    @Autowired
    private EmployeeExpenseService employeeExpenseService;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            // 统计会计凭证数量
            long journalEntryCount = journalEntryService.count();
            statistics.put("journalEntryCount", journalEntryCount);

            // 统计采购订单数量
            long purchaseOrderCount = purchaseOrderService.count();
            statistics.put("purchaseOrderCount", purchaseOrderCount);

            // 统计销售单数量
            long salesInvoiceCount = salesInvoiceService.count();
            statistics.put("salesInvoiceCount", salesInvoiceCount);

            // 统计员工费用数量
            long employeeExpenseCount = employeeExpenseService.count();
            statistics.put("employeeExpenseCount", employeeExpenseCount);

            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取最新业务通知
     */
    @GetMapping("/notifications")
    public Result<Map<String, Object>> getNotifications() {
        Map<String, Object> notifications = new HashMap<>();
        
        try {
            // 获取最新的3条会计凭证
            notifications.put("journalEntries", journalEntryService.getLatest(3));

            // 获取最新的3条采购订单
            notifications.put("purchaseOrders", purchaseOrderService.getLatest(3));

            // 获取最新的3条销售单
            notifications.put("salesInvoices", salesInvoiceService.getLatest(3));

            return Result.success(notifications);
        } catch (Exception e) {
            return Result.error("获取通知数据失败：" + e.getMessage());
        }
    }
}

