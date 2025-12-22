package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.SalesInvoice;
import com.finance.service.SalesInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sales-invoice")
public class SalesInvoiceController {
    
    @Autowired
    private SalesInvoiceService salesInvoiceService;

    @GetMapping("/list")
    public Result<List<SalesInvoice>> list() {
        return Result.success(salesInvoiceService.findAll());
    }

    @GetMapping("/{id}")
    public Result<SalesInvoice> getById(@PathVariable Long id) {
        SalesInvoice invoice = salesInvoiceService.findById(id);
        if (invoice != null) {
            return Result.success(invoice);
        }
        return Result.error("销售单不存在");
    }

    @GetMapping("/status/{status}")
    public Result<List<SalesInvoice>> getByStatus(@PathVariable String status) {
        return Result.success(salesInvoiceService.findByStatus(status));
    }

    @PostMapping("/save")
    public Result<SalesInvoice> save(@RequestBody SalesInvoice invoice) {
        try {
            SalesInvoice saved = salesInvoiceService.save(invoice);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            salesInvoiceService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}







