package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.PurchaseOrder;
import com.finance.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/purchase-order")
public class PurchaseOrderController {
    
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping("/list")
    public Result<List<PurchaseOrder>> list() {
        return Result.success(purchaseOrderService.findAll());
    }

    @GetMapping("/{id}")
    public Result<PurchaseOrder> getById(@PathVariable Long id) {
        PurchaseOrder order = purchaseOrderService.findById(id);
        if (order != null) {
            return Result.success(order);
        }
        return Result.error("采购订单不存在");
    }

    @GetMapping("/status/{status}")
    public Result<List<PurchaseOrder>> getByStatus(@PathVariable String status) {
        return Result.success(purchaseOrderService.findByStatus(status));
    }

    @PostMapping("/save")
    public Result<PurchaseOrder> save(@RequestBody PurchaseOrder order) {
        try {
            PurchaseOrder saved = purchaseOrderService.save(order);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            purchaseOrderService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}







