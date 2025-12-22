package com.finance.controller;

import com.finance.common.Result;
import com.finance.entity.Supplier;
import com.finance.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/list")
    public Result<List<Supplier>> list() {
        return Result.success(supplierService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Supplier> getById(@PathVariable Long id) {
        Supplier supplier = supplierService.findById(id);
        if (supplier != null) {
            return Result.success(supplier);
        }
        return Result.error("供应商不存在");
    }

    @GetMapping("/status/{status}")
    public Result<List<Supplier>> getByStatus(@PathVariable String status) {
        return Result.success(supplierService.findByStatus(status));
    }

    @GetMapping("/search")
    public Result<List<Supplier>> search(@RequestParam String name) {
        return Result.success(supplierService.searchByName(name));
    }

    @PostMapping("/save")
    public Result<Supplier> save(@RequestBody Supplier supplier) {
        try {
            Supplier saved = supplierService.save(supplier);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            supplierService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}







